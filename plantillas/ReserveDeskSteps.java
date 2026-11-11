package es.ejemplo.biblioteca.acceptance;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import es.ejemplo.biblioteca.application.ReserveDeskUseCase;
import es.ejemplo.biblioteca.domain.Desk;
import es.ejemplo.biblioteca.domain.DeskRepository;

/*PASOS DEL ESCENARIO

Scenario: Reserve an available desk
    Given desk "A1" exists and is available
    When I reserve desk "A1"
    Then the reservation is accepted
    And desk "A1" is reserved

  */

public class ReserveDeskSteps {
    private final Map<String, Desk> desks = new HashMap<>();
    private final DeskRepository repository = id -> Optional.ofNullable(desks.get(id));
    private final ReserveDeskUseCase useCase = new ReserveDeskUseCase(repository);
    private boolean accepted;

    @Given("desk {string} exists and is available")
    public void deskExistsAndIsAvailable(String deskId) {
        desks.put(deskId, new Desk(deskId, false));
    }

    @When("I reserve desk {string}")
    public void reserveDesk(String deskId) {
        accepted = useCase.reserve(deskId);
    }

    @Then("the reservation is accepted")
    public void reservationIsAccepted() {
        assertTrue(accepted);
    }

    @Then("the reservation is rejected")
    public void reservationIsRejected() {
        assertFalse(accepted);
    }
}
