package ch.bfh.bti7081.s2020.blue.domain.association.patientchallenge;

import ch.bfh.bti7081.s2020.blue.domain.Challenge;
import ch.bfh.bti7081.s2020.blue.domain.Patient;
import java.io.Serializable;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class PatientHasChallengeId implements Serializable {

  private static final long serialVersionUID = 1L;

  private Patient patient;
  private Challenge challenge;

  public Patient getPatient() {
    return this.patient;
  }

  public void setPatient(final Patient patient) {
    this.patient = patient;
  }

  public Challenge getChallenge() {
    return this.challenge;
  }

  public void setChallenge(final Challenge challenge) {
    this.challenge = challenge;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof PatientHasChallengeId
        && getPatient().equals(((PatientHasChallengeId) o).getPatient())
        && getChallenge().equals(((PatientHasChallengeId) o).getChallenge());
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(getPatient())
        .append(getChallenge())
        .build();
  }
}
