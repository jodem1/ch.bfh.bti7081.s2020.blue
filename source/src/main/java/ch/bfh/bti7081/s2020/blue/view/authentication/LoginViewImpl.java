package ch.bfh.bti7081.s2020.blue.view.authentication;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.Collections;

@PageTitle("Login")
@Route(value = LoginViewImpl.ROUTE)
public class LoginViewImpl extends VerticalLayout implements BeforeEnterObserver, LoginView {

  static final String ROUTE = "login";

  private final LoginForm login = new LoginForm();

  public LoginViewImpl() {
    login.setAction("login");

    // i18n
    final LoginI18n i18n = LoginI18n.createDefault();
    i18n.setHeader(new LoginI18n.Header());
    i18n.getHeader().setTitle("Login");
    i18n.getForm().setUsername("Benutzername");
    i18n.getForm().setTitle("Login");
    i18n.getForm().setSubmit("Login");
    i18n.getForm().setPassword("Passwort");
    i18n.getForm().setForgotPassword("Passwort vergessen");
    login.setI18n(i18n);

    add(login);
    setSizeFull();
    setHorizontalComponentAlignment(Alignment.CENTER, login);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) { //
    // inform the user about an authentication error
    // (yes, the API for resolving query parameters is annoying...)
    if (!event.getLocation().getQueryParameters().getParameters().getOrDefault("error", Collections.emptyList()).isEmpty()) {
      login.setError(true); //
    }
  }
}
