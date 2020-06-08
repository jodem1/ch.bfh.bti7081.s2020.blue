package ch.bfh.bti7081.s2020.blue.service;

import ch.bfh.bti7081.s2020.blue.domain.Challenge;
import ch.bfh.bti7081.s2020.blue.domain.Login;
import ch.bfh.bti7081.s2020.blue.domain.Patient;
import ch.bfh.bti7081.s2020.blue.domain.association.patientchallenge.PatientHasChallenge;
import ch.bfh.bti7081.s2020.blue.domain.dto.ChallengeDto;
import ch.bfh.bti7081.s2020.blue.domain.repository.ChallengeCrudRepository;
import ch.bfh.bti7081.s2020.blue.domain.repository.CurrentLoginRepository;
import ch.bfh.bti7081.s2020.blue.domain.repository.PatientHasChallengeCrudRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChallengeService {

  private static final Comparator<? super ChallengeDto> CHALLENGE_INCLOMPLETED_FIRST = Comparator.comparing(ChallengeDto::getCompleted);
  private static final Comparator<? super ChallengeDto> CHALLENGE_NOT_ACCEPTED_FIRST = Comparator.comparing(ChallengeDto::getAccepted);

  private final CurrentLoginRepository currentLoginRepository;
  private final ChallengeCrudRepository challengeCrudRepository;
  private final PatientHasChallengeCrudRepository patientHasChallengeCrudRepository;

  public ChallengeService(CurrentLoginRepository currentLoginRepository, ChallengeCrudRepository challengeCrudRepository, PatientHasChallengeCrudRepository patientHasChallengeCrudRepository) {
    this.currentLoginRepository = currentLoginRepository;
    this.challengeCrudRepository = challengeCrudRepository;
    this.patientHasChallengeCrudRepository = patientHasChallengeCrudRepository;
  }

  @Transactional(readOnly = true)
  public List<ChallengeDto> findAll() {
    return challengeCrudRepository.findAllInclusiveAccepted().stream()
        .map(this::challengeToDto)
        .sorted(CHALLENGE_INCLOMPLETED_FIRST)
        .sorted(CHALLENGE_NOT_ACCEPTED_FIRST)
        .collect(Collectors.toList());
  }

  private ChallengeDto challengeToDto(Challenge challenge) {
    return ChallengeDto.builder()
        .id(challenge.getId())
        .name(challenge.getName())
        .content(challenge.getContent())
        .criteria(challenge.getCriteria())
        .accepted(challenge.getPatients() != null && !challenge.getPatients().isEmpty())
        .completed(challenge.getPatients() != null && !challenge.getPatients().isEmpty() && challenge.getPatients().get(0).getCompleted())
        .build();
  }

  public List<Challenge> findAllAssignedToCurrentUser() {
    return challengeCrudRepository.findAllAssignedToCurrentUserAndNotCompleted();
  }

  public Challenge findById(Long id) {
    return challengeCrudRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException(String.format("Challenge with id %s not found", id)));
  }

  public void assignToCurrentUser(Long challengeId) {
    patientHasChallengeCrudRepository.assignToCurrentUser(challengeId);
  }

  @Transactional
  public void completeChallenge(Long challengeId) {
    Long patientId = currentLoginRepository.getCurrentLogin()
        .map(Login::getPatient)
        .map(Patient::getId)
        .orElseThrow(() -> new IllegalArgumentException("User not authenticated!"));
    Optional<PatientHasChallenge> patientHasChallenge = patientHasChallengeCrudRepository
        .findByPatientIdAndChallengeId(patientId, challengeId);
    patientHasChallenge
        .ifPresent((patientHasChallenge1 -> patientHasChallenge1.setCompleted(Boolean.TRUE)));
  }
}
