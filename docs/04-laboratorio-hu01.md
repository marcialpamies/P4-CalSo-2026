# Laboratorio guiado HU-01 · Registrar una propuesta para una sesión programada

**Práctica 4 · Calidad del Software — ATDD, BDD/Cucumber y TDD con JUnit 5**

## 0. Objetivo y punto de partida

En este laboratorio desarrollamos **HU-01**, desde el primer ejemplo de aceptación hasta la recuperación de las propuestas registradas. No partimos de una aplicación vacía: el proyecto Maven inicial de `ProyectoP4/` ya dispone de clases del dominio de P3, de un puerto de consulta de dojos y de una implementación en memoria. **Ya dispone de sesiones programadas como parte del agregado `Dojo`**, pero no de propuestas ni de los casos de uso de P4.

**Historia acordada:** Como entrenador que prepara una sesión, quiero registrar una propuesta manual para una sesión programada que he seleccionado y consultarla después, para conservar únicamente las propuestas que se ajustan a las condiciones de esa sesión.

El primer escenario es **BDD-01a**: registrar `PROP-001`, de 45 minutos, para `SES-001`, programada en `DOJO-01` con duración máxima de 60 minutos. La propuesta aceptada debe recuperarse con **`dojoId = DOJO-01` y `sessionId = SES-001`**.

**Qué no se desarrolla:** creación, programación o persistencia real de sesiones; calendario y reservas; aplicación web; cliente IA; ejercicios técnicos o físicos (aparecen en HU-02). Ningún ejemplo de HU-01 exige comprobar nivel o armas, pero esos datos pertenecen a la sesión y a su dojo, y serán relevantes después.

## 1. Preparación del repositorio y primer punto de control

Trabajad sobre la copia del **proyecto base**. Antes de añadir Cucumber o código nuevo:

1. Comprobad que estáis en la rama de integración del grupo, que contiene `ProyectoP4/` con las clases de partida y las pruebas heredadas.
2. Ejecutad `mvn -f ProyectoP4/pom.xml clean verify`. Confirmad que pasa la regresión inicial y anotad el número de pruebas **realmente ejecutadas**; al comienzo aún no hay escenarios Cucumber.

### Convención obligatoria de commits del laboratorio

El mensaje se escribe **en español**, salvo los identificadores técnicos, y sigue siempre esta estructura:

```text
TIPO HU-01 BDD-XX: descripción concreta del cambio
```

`TIPO` identifica **la técnica y la naturaleza del commit**. El identificador BDD se refiere al escenario que impulsa el cambio, incluso cuando se está creando una prueba JUnit. Para trabajo que abarque varios escenarios, indicadlos todos, por ejemplo `BDD-02+BDD-02b`. Usad las siguientes formas de manera consistente:

| Prefijo | Se utiliza cuando… | Ejemplo de mensaje completo |
|---|---|---|
| `ATDD-TEST` | Se escribe o amplía la *feature* o se prepara la especificación ejecutable; puede quedar `undefined` o `pending` | `ATDD-TEST HU-01 BDD-01a: definir aceptación y consulta de la propuesta` |
| `ATDD-FEAT` | Se implementan los pasos `Given/When/Then` para ejecutar la aceptación contra el caso de uso real | `ATDD-FEAT HU-01 BDD-01a: automatizar registro y recuperación en Cucumber` |
| `TDD-TEST` | Se incorpora primero una prueba JUnit nueva que especifica una conducta | `TDD-TEST HU-01 BDD-03: exigir rechazo de duración cero` |
| `TDD-FEAT` | Se introduce el código mínimo necesario para satisfacer el test precedente | `TDD-FEAT HU-01 BDD-03: impedir registrar duraciones no positivas` |
| `TDD-REFACTOR` | Se reorganiza producción **y, si corresponde, sus pruebas** sin cambiar los comportamientos acordados | `TDD-REFACTOR HU-01 BDD-02+BDD-03: unificar el resultado de register` |

**Cuándo crear el commit:** inmediatamente **después de escribir y ejecutar** el cambio que representa, y **antes de empezar el siguiente tipo de trabajo**. Para `TDD-TEST`, guardad la prueba en su RED real (fallo por aserción o por API inexistente). Para `TDD-FEAT`, ejecutad los tests afectados y comprobad GREEN. Para `TDD-REFACTOR`, exigid GREEN antes y después; modificad juntos código y pruebas en **un único commit lógico**. Cuando se complete el glue de un escenario, registrad su `ATDD-FEAT` tras comprobar los `Then` y el escenario GREEN. La creación de definiciones todavía `pending` se puede versionar como un `ATDD-TEST` adicional que lo indique en su mensaje.

**No inventéis un RED** para los valores que ya satisfaga el código, ni generéis commits REFACTOR sin mejora real. `undefined` o `pending` acreditan el ciclo exterior ATDD, **no** un RED JUnit. Si una nueva prueba no compila porque aún no existe el contrato de producción, señalad expresamente «RED de compilación»; no afirméis que ha fallado una aserción.

**Cómo registrar un hito** (sustituir el mensaje por el correspondiente y seleccionar solo archivos relacionados):

```bash
git status
git add ProyectoP4/src/test
git diff --cached
git commit -m "TDD-TEST HU-01 BDD-03: exigir rechazo de duración cero"
git log -1 --oneline
```

El ejemplo anterior sirve para un commit **solo de pruebas**. Para GREEN o REFACTOR, añadid también los archivos de producción modificados con `git add`, revisad `git diff --cached` y confirmad que no se incluyen archivos ajenos al ciclo. El commit es **local**; para publicar el historial de vuestra rama ejecutad `git push -u origin`. 

## 2. Inventario real del proyecto Maven

| Ya existe en la plantilla | Ubicación y utilización |
|---|---|
| `Dojo`, `Weapon` | `src/main/java/.../domain/model/`; dojo identificado y catálogo de armas |
| `Money`, `Discount` | Se conservan por compatibilidad con el `Dojo` de P3; **no intervienen en la duración** |
| `DojoRepository` | `src/main/java/.../domain/port/`; permite consultar `DOJO-01` por identificador |
| `InMemoryDojoRepository` | `src/main/java/.../infrastructure/persistence/`; catálogo de dojos listo para pruebas |
| Pruebas JUnit anteriores | `src/test/java/.../`; constituyen la regresión inicial |
| Maven + JUnit + Cucumber + Mockito + Jackson + JaCoCo | Dependencias en `P4/pom.xml`; **todavía no se han creado features ni glue** |

Antes de modificar el código, ejecutar desde la raíz del repositorio:

```bash
mvn -f ProyectoP4/pom.xml clean verify
```

Desde Eclipse: **File → Import → Maven → Existing Maven Projects**, seleccionar la carpeta `ProyectoP4`; después, **Run As → Maven build...**, con el objetivo `clean verify`. Comprobar que se ejecutan las pruebas heredadas y que no hay errores de compilación. **Esta comprobación inicial no demuestra que Cucumber funcione: aún no existe una feature.** No modificar el POM o los tests antiguos para simular un éxito.

## 3. Seleccionar una sesión ya programada en el agregado Dojo

Las sesiones se programan fuera del contexto acotado de planificación. P4 **recibe la identidad de la sesión seleccionada** y necesita conocer sus condiciones para evaluar la propuesta.

| Información de referencia | Valor del ejemplo |
|---|---|
| Identificador de sesión | `SES-001` |
| Dojo al que pertenece | `DOJO-01` |
| Duración máxima | 60 minutos |
| Nivel del grupo | `BEGINNER` |
| Armas del dojo | `BO`, `KATANA` (proceden de `DojoRepository`) |

**Invariante de negocio:** una propuesta registrada para `SES-001` no puede evaluarse usando la duración máxima de una sesión distinta ni un dojo elegido de forma independiente. La propuesta conserva **ambos identificadores `(dojoId, sessionId)`** de destino, recuperados mediante el agregado `Dojo`; el dojo se selecciona primero y la sesión se localiza dentro de él. No se presupone que `sessionId` sea globalmente único.

### Estructura del dominio que ya se entrega

- **`Dojo` es la raíz del agregado** del catálogo heredado. Mantiene sus armas y el conjunto de sesiones ya programadas.
- **`TrainingSession` y `TrainingLevel` ya existen** en `domain/model`. La sesión contiene `id`, `maximumDurationMinutes` y `level`. Su pertenencia al dojo se establece por la colección de sesiones de `Dojo`, sin duplicar un `dojoId` modificable dentro de la sesión.
- **`DojoRepository` ya existe** y ofrece `findById(dojoId)`. `InMemoryDojoRepository` contiene `DOJO-01` con `SES-001`, máximo 60 minutos y nivel BEGINNER.
- **`TrainingProposal`, los resultados y el puerto de propuestas no existen todavía**; sus responsabilidades deben aparecer desde las pruebas.

La consulta inicial, **sobre código entregado**, es:

```java
DojoRepository catalog = new InMemoryDojoRepository();
Dojo dojo = catalog.findById("DOJO-01").orElseThrow();
TrainingSession session = dojo.findSessionById("SES-001").orElseThrow();
// session.maximumDurationMinutes() == 60
```

No se implementa un `TrainingSessionRepository`, no se programan sesiones y el sensei no puede proporcionar un máximo alternativo. El `Given` de Cucumber **recupera** los datos del catálogo en lugar de construir arbitrariamente otra sesión. La nueva propuesta pertenecerá al contexto de planificación y **referenciará `(dojoId, sessionId)`**, sin guardarse dentro del agregado `Dojo`.

## 4. Ciclo exterior ATDD: BDD-01a

Crear `src/test/resources/features/hu01-register-proposal.feature` y escribir el primer escenario **en inglés**, con el siguiente comportamiento acordado:

```gherkin
Feature: Register a training proposal for a scheduled session

  Scenario: BDD-01a - Register a proposal that fits the selected session
    Given dojo "DOJO-01" has the scheduled session "SES-001"
    When the sensei registers proposal "PROP-001" of 45 minutes for session "SES-001"
    Then the proposal is accepted
    And proposal "PROP-001" can be retrieved with 45 minutes for dojo "DOJO-01" and session "SES-001"
```

El nivel `BEGINNER` y las armas ya están disponibles en el agregado `Dojo` proporcionado, aunque HU-01 aún no los valide. **No añadir un paso que cree una reserva de calendario**. Tampoco basta con comprobar que se devuelve `true`: es necesario recuperar la propuesta.

### Preparar la ejecución de Cucumber

1. Crear una clase de suite en `src/test/java/` cuyo nombre termine en `Test`, por ejemplo `Hu01AcceptanceSuiteTest`, con `@Suite`, `@IncludeEngines("cucumber")`, `@SelectClasspathResource("features")` y `@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "<paquete-del-glue>")`. Esas anotaciones pertenecen a JUnit Platform Suite y `GLUE_PROPERTY_NAME` a Cucumber.
2. Crear el paquete de glue en `src/test/java/`, separado de las clases de producción. No implementar lógica de negocio dentro de los métodos `Given/When/Then`.
3. Ejecutar la suite desde Eclipse (**Run As → JUnit Test**) o Maven (**`mvn -f P4/pom.xml test`**). Comprobar que se descubre realmente **un escenario**; el mensaje `undefined` indica que faltan definiciones de pasos, no que el dominio haya fallado.
4. Introducir definiciones con `io.cucumber.java.en.Given`, `When` y `Then`. Inicialmente pueden lanzar `io.cucumber.java.PendingException`. Volver a ejecutar: `pending` tampoco es todavía una prueba JUnit RED del comportamiento.
5. Preparar en los `Given` el catálogo de dojos ya proporcionado y los datos controlados de la sesión. **No escribir todavía el código de aceptación dentro del glue**.

**Commits de esta etapa:**

- Tras escribir la feature y la suite, ejecutar Cucumber y comprobar el estado `undefined`. Commit: `ATDD-TEST HU-01 BDD-01a: definir escenario y suite de aceptación`.
- Tras añadir las definiciones iniciales de pasos, volver a ejecutar y comprobar que el escenario alcanza `pending` en un paso realmente descubierto. Commit: `ATDD-TEST HU-01 BDD-01a: preparar glue pendiente de implementación`.
- Todavía **no** crear el commit `ATDD-FEAT`: falta completar la ejecución de los pasos contra el caso de uso y obtener la aceptación esperada.

**Atención:** la suite debe llamarse y configurarse de modo que Surefire la descubra. No aceptar «BUILD SUCCESS» con cero escenarios como evidencia de ATDD. Un resultado `undefined` o `pending` se documenta como estado inicial, no como aceptación satisfactoria.

## 5. Primer microciclo JUnit: registrar una propuesta

**Objetivo del primer test de aplicación:** registrar `PROP-001` de 45 minutos para `SES-001`, previamente conocida y con máximo 60, y obtener aceptación.

1. Escribir un método JUnit con nombre `testGivenScheduledSessionOf60MinutesWhenRegister45MinuteProposalThenAccepted`.
2. Definir desde la prueba qué datos necesita la entrada. Si el test obliga a introducir una nueva clase o contrato que aún no existe, **registrar el RED de compilación genuino**.
3. Implementar la mínima coordinación para que el caso válido pase; mantener en pruebas los datos de la sesión de referencia y no resolver toda la HU de una vez.
4. Ejecutar `Run As → JUnit Test` sobre la clase JUnit o `mvn -f P4/pom.xml test`. Guardar el resultado GREEN y revisar si la regla ya merece un objeto de dominio.

**Hitos Git obligatorios del primer microciclo:**

- **Antes del código de producción**, guardar la prueba JUnit y ejecutar. Si falla de forma genuina, commit `TDD-TEST HU-01 BDD-01a: exigir aceptación de una propuesta de 45 minutos`. Describir si el RED es de compilación por ausencia de contrato o una aserción fallida.
- Implementar solo el comportamiento que permite superar ese test, ejecutar de nuevo y guardar `TDD-FEAT HU-01 BDD-01a: aceptar propuesta ajustada a la sesión`. Comprobar que la duración máxima procede del dojo y su sesión, **no de una constante**.
- No crear una segunda operación pública anticipándose a futuros códigos de rechazo. Una devolución inicial simple, como un booleano, puede ser suficiente **provisionalmente** para este test; el contrato evolucionará cuando los rechazos exijan motivos.

Si la implementación deja una responsabilidad impropia o duplicidad visible, justificar la mejora y registrar un `TDD-REFACTOR` tras comprobar GREEN; no es obligatorio inventar uno en este primer microciclo.

**Pregunta clave:** ¿quién conoce el máximo de 60 minutos? No lo decide la propuesta, ni debe convertirse en un número fijo de la aplicación. Debe proceder de las condiciones de `SES-001` usadas en el test.

## 6. Segundo microciclo: vínculo y recuperación

La aceptación por sí sola no demuestra el registro. Abrir un nuevo test que exija **recuperar `PROP-001` con `dojoId = DOJO-01`, `sessionId = SES-001` y duración 45**.

- Hacer emerger, si lo requiere la prueba, una responsabilidad de almacenamiento de propuestas: **no reutilizar `DojoRepository` para guardar propuestas**.
- Para comprobar recuperación, emplear un fake con estado o una implementación sencilla en memoria a través del puerto que haya surgido.
- Para comprobar una interacción concreta, un mock podría servir; **verificar una llamada `save` no demuestra por sí solo la recuperación**.
- Asegurarse de que se conservan **`dojoId` y `sessionId`**, sin tratar `sessionId` como identificador global ni perder la relación al guardar.
- Completar el `When` y todos los `Then` del escenario. Ejecutar Cucumber hasta obtener **BDD-01a GREEN** y pasar toda la regresión.

**Commits:** registrar `TDD-TEST HU-01 BDD-01a: exigir recuperación de la propuesta y sus identificadores` antes de crear un modelo o puerto nuevo, si la prueba falla realmente; después `TDD-FEAT HU-01 BDD-01a: guardar y recuperar propuestas mediante su repositorio`. Completar los cuatro pasos de Cucumber y registrar `ATDD-FEAT HU-01 BDD-01a: verificar aceptación y recuperación mediante Cucumber` **solo cuando ese escenario esté GREEN**. La recuperación debe probarse observando el estado registrado, no solo una llamada al puerto.

**Revisión OO:** ¿se están mezclando las responsabilidades de localizar la sesión, comprobar la duración y almacenar una propuesta? No es obligatorio resolverlas con tres clases inmediatamente: registrar la pregunta y refactorizar cuando los tests lo justifiquen.

## 7. Completar los límites de HU-01 en el orden acordado

Mantener `SES-001` / `DOJO-01` (máximo 60). Cada caso debe incluir aceptación/rechazo observable y consulta del estado de almacenamiento.

| Escenario | Propuesta | Duración | Resultado |
|---|---|---:|---|
| BDD-01b | `PROP-004` | 59 | Aceptada, recuperable con `dojoId = DOJO-01` y `sessionId = SES-001` |
| BDD-01c | `PROP-005` | 60 | Aceptada, recuperable con `dojoId = DOJO-01` y `sessionId = SES-001` |
| BDD-01d | `PROP-006` | 1 | Aceptada, recuperable con `dojoId = DOJO-01` y `sessionId = SES-001` |
| BDD-02 | `PROP-002` | 75 | Rechazada: `PROPOSAL_EXCEEDS_AVAILABLE_DURATION`; no registrada |
| BDD-02b | `PROP-007` | 61 | Mismo rechazo; no registrada |
| BDD-03 | `PROP-003` | 0 | Rechazada: `INVALID_PROPOSAL_DURATION`; no registrada |

Trabajar cada escenario mediante el ciclo **ATDD (escenario antes de producción) → JUnit RED (solo cuando aparezca conducta nueva) → GREEN mínimo → refactorización con regresión (si procede) → Cucumber GREEN**. Los valores 59, 60 y 1 pueden pasar con la implementación del primer escenario; si ocurre, **no provocar fallos artificiales**.

### Recorrido de commits para los casos límite y los rechazos

1. Incorporar los escenarios de aceptación adicionales a la feature. Versionar, por ejemplo, `ATDD-TEST HU-01 BDD-01b+BDD-01c+BDD-01d: especificar valores admitidos`. Si se añaden varios casos con `Scenario Outline`, indicar todos sus IDs en el mensaje. Los casos que ya pasan constituyen **pruebas de regresión/triangulación**, no un nuevo RED que deba forzarse.
2. Incorporar `BDD-02` (75), `BDD-02b` (61) y `BDD-03` (0). Registrar primero la especificación: `ATDD-TEST HU-01 BDD-02+BDD-02b+BDD-03: especificar rechazos por duración`.
3. Para la **duración cero**, escribir antes de producción un test que compruebe **rechazo y ausencia de almacenamiento**. Ejecutarlo, observar el RED real y registrar `TDD-TEST HU-01 BDD-03: exigir rechazo de una duración cero sin registro`. Corregir con el mínimo código y comprobar GREEN: `TDD-FEAT HU-01 BDD-03: impedir el registro de duración cero`.
4. Para el exceso, comprobar tanto el caso 75 como el límite inmediato 61. Si la regla `duration > maximumDurationMinutes` ya hace pasar ambos, **no crear un RED artificial**. Son pruebas que consolidan el comportamiento existente.
5. Abrir la siguiente necesidad con JUnit: ahora **no basta aceptar o rechazar**, hay que comunicar `INVALID_PROPOSAL_DURATION` o `PROPOSAL_EXCEEDS_AVAILABLE_DURATION`, sin guardar las rechazadas. Versionar la prueba que exija el resultado informativo con `TDD-TEST HU-01 BDD-02+BDD-03: exigir motivos explícitos de rechazo`. Si necesita una API todavía inexistente, conservar el diagnóstico como RED de compilación. Implementar la mínima ampliación del resultado y registrar `TDD-FEAT HU-01 BDD-02+BDD-03: informar del motivo del rechazo`.
6. **REFACTOR obligatorio si ha aparecido una duplicidad de operaciones:** si el primer microciclo introdujo `boolean register(...)` y la ampliación creó `registerWithOutcome(...)`, no conservar ambas por comodidad. Una vez GREEN, dejar **un único método público `register(...)` que devuelva el resultado informativo**. Actualizar todas las pruebas JUnit y el glue para usarlo, sin alterar los valores esperados. Comprobar la regresión y registrar en **un solo commit lógico**: `TDD-REFACTOR HU-01 BDD-02+BDD-03: unificar register y actualizar sus pruebas`. La refactorización no exige provocar un nuevo RED ni cambiar los criterios de aceptación; el proyecto puede no haber necesitado nunca un wrapper booleano.
7. Completar los `Then` de todos los escenarios Cucumber, verificando el motivo exacto, la ausencia de registro en rechazos y `dojoId`/`sessionId` en aceptaciones. Tras ejecutar **los siete escenarios y la regresión**, registrar `ATDD-FEAT HU-01 BDD-01a+BDD-01b+BDD-01c+BDD-01d+BDD-02+BDD-02b+BDD-03: completar aceptación`. Se permite distribuir este paso en commits menores por escenarios, siempre con IDs claros.

**Un commit no se denomina `TDD-REFACTOR` si introduce una regla nueva o corrige una aserción incorrecta:** en esos casos corresponde un nuevo par `TDD-TEST` / `TDD-FEAT`. No se cambia la expectativa de los tests para conseguir GREEN; únicamente se adapta su llamada cuando mejora el contrato interno del caso de uso.

**No romper HU-01 al implementar HU-02:** los registros básicos de esta guía no especifican ejercicios; HU-02 añade propuestas detalladas con una lista expresa de ejercicios, que debe ser no vacía. BDD-05c no invalida los siete ejemplos básicos. La forma Java de distinguir ambas entradas emergerá en las pruebas posteriores.

La secuencia de causas de rechazo se incorpora a partir de los escenarios que las exigen. No añadir códigos extra ni validar requisitos no pactados (IDs duplicados, horarios o sesiones inexistentes).

## 8. Diseño emergente y preparación de HU-02

Al concluir HU-01 hay una propuesta recuperable y vinculada a una sesión determinada. Revisar el diseño a la luz de las pruebas:

1. ¿Existe una responsabilidad de dominio clara para la duración y el vínculo a la sesión?
2. ¿Puede el caso de uso obtener las condiciones de `SES-001` sin que el sensei le imponga otro límite de duración?
3. ¿El repositorio de propuestas se distingue del `DojoRepository` de P3? ¿Por qué la propuesta no se almacena dentro del agregado `Dojo`?
4. El nivel del grupo **ya figura en `TrainingSession`**. ¿Cómo evitar que HU-01 incorpore prematuramente la validación de intensidades que corresponde a HU-02?
5. ¿El diseño permitirá incorporar una lista de ejercicios en HU-02 sin reescribir retroactivamente los escenarios de HU-01?

No imponer antes de los tests una jerarquía de ejercicios ni un `RegistrationResult` con todos los atributos futuros. Si el nuevo test de motivo de rechazo necesita ampliar el resultado, hacerlo entonces. **Al terminar HU-01, revisad si quedan dos métodos de registro con la misma finalidad o pruebas que dependan de una API provisional; unificad el contrato cuando sea seguro sin cambiar los comportamientos aceptados.** Un único `register` con un resultado que distinga aceptación y causas de rechazo es una solución coherente, no una obligación de crear un tipo Java con un nombre predeterminado.

**Cierre de refactorización:** verificar que el nuevo contrato no rompe las pruebas heredadas, las JUnit de HU-01 ni ningún `Then` Cucumber. El diagrama UML final debe describir el contrato **que realmente queda**, no cada forma provisional que tuvo durante los microciclos.

### UML progresivo

Guardar el diagrama correspondiente al **commit real de cierre de HU-01**. Representar `Dojo` y `TrainingSession` como **elementos entregados** y añadir al UML únicamente las clases y relaciones **nuevas** que efectivamente hayan surgido en el código. No atribuir al trabajo TDD del alumno la creación de clases preexistentes. La fecha y las reservas quedan fuera del límite de P4.

## 9. Evidencia y cierre del laboratorio

En `README_P4.md` registrar: historia y escenario, estado Cucumber, test JUnit introducido, diagnóstico RED real, mínimo GREEN, cambio de diseño, nueva comprobación y **hash permanente del commit**. No simular fallos ni fabricar un REFACTOR.

| HU / escenario | Fase y resultado observado | Mensaje de commit | Hash | Prueba o ejecución vinculada |
|---|---|---|---|---|
| HU-01 / BDD-01a | ATDD: escenario `undefined` | `ATDD-TEST HU-01 BDD-01a: definir aceptación` | *hash real* | *Cucumber / Actions* |
| HU-01 / BDD-01a | TDD: RED por contrato inexistente | `TDD-TEST HU-01 BDD-01a: exigir aceptación` | *hash real* | *JUnit / diagnóstico* |
| HU-01 / BDD-01a | TDD: GREEN mínimo | `TDD-FEAT HU-01 BDD-01a: aceptar propuesta válida` | *hash real* | *JUnit / ejecución* |
| HU-01 / BDD-02+BDD-03 | TDD: REFACTOR sin modificar reglas | `TDD-REFACTOR HU-01 BDD-02+BDD-03: unificar register` | *hash real* | *Regresión* |
| HU-01 / BDD-01a–BDD-03 | ATDD: los siete escenarios GREEN | `ATDD-FEAT HU-01 BDD-01a+BDD-01b+BDD-01c+BDD-01d+BDD-02+BDD-02b+BDD-03: completar aceptación` | *hash real* | *Maven / Actions* |

La tabla es **ilustrativa**: registrar solo los hitos y diagnósticos que realmente se hayan producido. Guardar también el **hash final de HU-01**, el enlace de la ejecución completa y el UML correspondiente. Los commits RED y PENDING pueden fallar en CI; deben permanecer identificables en la rama de desarrollo y **no integrarse** sin completar los ciclos.

**Auditar con Git (raíz del repositorio):**

```bash
git log --oneline --decorate -- P4/
git show --stat HASH_REAL
git show HASH_REAL -- P4/
git diff HASH_ANTERIOR HASH_NUEVO -- P4/
```

Sustituid los marcadores por hashes verdaderos. En GitHub, abrid el historial de la rama, seleccionad el commit y consultad la ejecución de Actions asociada. En Eclipse, utilizad `Team → Show in History` y `Compare with Each Other` entre dos revisiones. Evitad `squash` al integrar si destruye el recorrido que debéis explicar.

**Criterios de finalización:**
- [ ] `mvn -f P4/pom.xml clean verify` ejecuta JUnit y Cucumber sin fallos; no hay escenarios `pending` ni `undefined`.
- [ ] Los siete ejemplos de HU-01 se ejecutan y producen el resultado acordado.
- [ ] `PROP-001` y las otras propuestas admitidas se recuperan con **su misma pareja `dojoId`/`sessionId`**.
- [ ] Las propuestas rechazadas no pueden recuperarse.
- [ ] Se ha recuperado `SES-001` del agregado `Dojo` existente, sin crear ni almacenar sesiones y sin mezclar el repositorio de dojos con el de propuestas.
- [ ] El contrato final de registro distingue aceptación y los dos motivos pactados, sin mantener innecesariamente una operación booleana paralela.
- [ ] Existen commits auténticos y bien nombrados que distinguen ATDD-TEST, ATDD-FEAT, TDD-TEST, TDD-FEAT y, cuando haya refactorización real, TDD-REFACTOR.
- [ ] El README enlaza hashes, diagnósticos, CI y UML, y los commits permiten reconstruir la evolución real de la solución.

**Eclipse:** importar como proyecto Maven; **Run As → Maven build...** con `clean verify`; sobre cada test JUnit o suite Cucumber, **Run As → JUnit Test**. Si falla el descubrimiento de Cucumber, comprobar las dependencias de JUnit Platform Suite, el nombre de la suite, el classpath de la feature y el glue antes de atribuirlo a reglas del dominio.

**Relevo posterior:** el primer alumno desarrollará también HU-02 hasta BDD-05d antes de entregar al segundo el commit integrado y GREEN. HU-01 es el primer corte UML de esa etapa, no el hito de relevo entre participantes.
