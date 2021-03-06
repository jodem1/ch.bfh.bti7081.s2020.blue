package ch.bfh.bti7081.s2020.blue.view.therapist;

import ch.bfh.bti7081.s2020.blue.domain.Therapist;
import ch.bfh.bti7081.s2020.blue.presenter.TherapistListPresenter;
import ch.bfh.bti7081.s2020.blue.util.BeanInjector;
import ch.bfh.bti7081.s2020.blue.view.layout.SocialAnxietyLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import java.util.List;

@Route("therapists")
@CssImport("./styles.css")
public class TherapistListViewImpl extends SocialAnxietyLayout implements TherapistListView {

  private Div content;

  public TherapistListViewImpl(BeanInjector beanInjector) {
    super(beanInjector);

    TherapistViewListener listener = new TherapistListPresenter(this, beanInjector);
    listener.onInit();
  }

  @Override
  protected void initializeView(BeanInjector beanInjector) {
    content = new Div();
    add(content);
  }

  @Override
  public void display(List<Therapist> therapists) {
    content.add(new H2("Therapeuten für soziale Angststörungen"));

    for (Therapist therapist : therapists) {
      HorizontalLayout horizontalLayout = new HorizontalLayout();
      VerticalLayout verticalLayout = new VerticalLayout();
      Icon icon = new Icon(VaadinIcon.DOCTOR);
      icon.setSize("10em");
      icon.setColor("#000000");
      horizontalLayout.add(icon, verticalLayout);

      H4 name = new H4(therapist.getTitle() + " " + therapist.getSurname() + " " + therapist.getGivenName());
      Label street = new Label(therapist.getStreet());
      Label place = new Label(therapist.getPlace());
      Label information = new Label(therapist.getInformation());
      verticalLayout.setSpacing(false);
      verticalLayout.add(name, street, place, information);

      content.add(horizontalLayout);
    }
  }
}
