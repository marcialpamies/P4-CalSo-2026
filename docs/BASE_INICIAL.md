# P4 · Proyecto Maven inicial

## Qué se reutiliza de P3

Se han conservado las clases de dominio `Dojo`, `Weapon`, `Money` y `Discount` de P3; `Dojo` **se ha ampliado** para contener las sesiones programadas sin romper el constructor histórico de cuatro argumentos y las cuatro clases de pruebas correspondientes. Se conserva el puerto **`DojoRepository`** del slice de búsqueda de dojos de P3. Se incorpora una implementación mínima `InMemoryDojoRepository`, pruebas del catálogo y pruebas de las sesiones ya programadas. `Dojo` actúa como **raíz del agregado** que comprende las armas disponibles y las sesiones preexistentes.

**No se ha trasladado** el slice de reservas, cálculo de costes, equipos, Fighter, Spring Boot, controladores web, Selenium, aplicación P3, ni repositorios que dependan de esas clases. `Money` y `Discount` no son reglas nuevas de entrenamiento: se conservan porque forman parte de la firma del `Dojo` original. Pueden no necesitarse en los casos de uso nuevos.

Datos del catálogo inicial:
- `DOJO-01` (Dojo Central): armas `BO` y `KATANA`.
- `DOJO-02` (Dojo Sur): arma `BO`.

Las sesiones **ya están programadas y vienen dadas en el catálogo inicial**. `TrainingSession` contiene identificador, duración máxima positiva y nivel; `TrainingLevel` contiene BEGINNER, INTERMEDIATE y ADVANCED. `Dojo` es la **raíz del agregado** y contiene las sesiones asociadas: `DOJO-01` incluye `SES-001`, con duración máxima de 60 minutos y nivel `BEGINNER`. `DojoRepository.findById("DOJO-01")` y `Dojo.findSessionById("SES-001")` permiten recuperar esa sesión. **El alumnado no desarrolla ni registra sesiones y tampoco construye un repositorio de sesiones**. La propuesta debe conservar la referencia completa **`(dojoId, sessionId)`** al agregado y a la sesión. El ID de sesión no se presupone único entre todos los dojos; se accede mediante `DojoRepository.findById(dojoId)` seguido de `Dojo.findSessionById(sessionId)`. La propuesta se almacenará separadamente y **no se añade a la colección de sesiones del dojo**; el diseño de las propuestas sí debe emerger en HU-01 mediante ATDD/TDD. El contrato de HU-01 admite propuestas básicas sin ejercicios especificados; en HU-02, una lista explícita de ejercicios no puede estar vacía. Consultar [la guía HU-01](docs/03-laboratorio-hu01.md).

## Herramientas incluidas

- Java 17; Maven; JUnit 5.
- Cucumber Java + motor JUnit Platform Suite para los escenarios que se añadirán.
- Mockito para dobles de prueba.
- Jackson Databind como librería de análisis JSON para HU-03; no hay adaptador preconstruido.
- JaCoCo para cobertura. Sin servicios web ni base de datos.

### Comprobación inicial

Desde la raíz del repositorio del grupo:

```bash
mvn -f P4/pom.xml clean verify
```

Desde el directorio `P4`:

```bash
mvn clean verify
```

Al comenzar solo se ejecutan las pruebas JUnit del dominio heredado y del catálogo. **La ausencia inicial de features Cucumber es intencionada.** El laboratorio proporcionará los pasos para añadir una feature, glue y runner de Cucumber una vez que exista un escenario real. No se debe crear una feature que solo compruebe `true`.

### Estructura de partida

```text
ProyectoP4/
├── pom.xml
├── src/
│   ├── main/java/es/um/calso/dojoreservation/
│   │   ├── domain/model/           Dojo, Weapon, Money, Discount,
│   │   │                           TrainingSession, TrainingLevel
│   │   ├── domain/port/            DojoRepository (P3)
│   │   └── infrastructure/persistence/
│   │                               InMemoryDojoRepository
│   └── test/java/es/um/calso/dojoreservation/
│       ├── domain/model/           Pruebas de P3 y de sesiones de Dojo
│       └── infrastructure/persistence/
│                                   InMemoryDojoRepositoryTest
└── BASE_ALUMNADO.md
```

Las capas `application` y el resto del dominio/infrastructure de P4 deben crearse únicamente cuando las pruebas lo requieran. No modificar los tests heredados solo para hacerlos pasar. El enunciado y contrato del cliente están en `docs/00-enunciado-alumnado.md`, `docs/01-contexto-backlog.md` y `docs/02-aceptacion.md`.
