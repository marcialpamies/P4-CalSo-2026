# Práctica 4 · Desarrollo de un vertical slice sobre propuestas de entrenamiento en dojos usando ATDD y TDD con Cucumber y pruebas orientadas a objetos.

## Descripción

El Ayuntamiento para el que se desarrollo la funcionalidad de reserva de dojos de la práctica P3 utiliza *Dojo Reservation* para organizar actividades de artes marciales. En esa práctica se trabajó con los conceptos: dojos, armas, equipos y reservas. Ahora se necesita una función nueva: ayudar a los entrenadores a preparar sesiones de entrenamiento.

Los dojos contienen sesiones de entrenamiento programadas. Cada sesión tiene un identificador, pertenece a un dojo concreto e indica la duración máxima y el nivel del grupo al que va dirigido. El sensei selecciona una de esas sesiones para registrar una propuesta de entrenamiento, comprobar que puede realizarse y consultarla después. También podrá pedir propuestas de entrenamiento a un asistente de inteligencia artificial. El sistema no debe aceptar una propuesta solo porque el asistente la haya generado, es el propio sensei el que termina aceptando o rechazando la propuesta.

Se va a desarrollar esta parte de la aplicación y sus pruebas. No hay que crear pantallas, conectar una base de datos ni contratar un servicio de IA externo. Se trabajará con Java, Maven y comprobaremos los casos de uso desde Cucumber.

Esta práctica se centra en **cómo se desarrolla y se verifica** el código. Es importante que el resultado funcione, pero también que podamos seguir las decisiones que se vayan tomando desde el primer escenario de aceptación hasta la ejecución final.

## Estado inicial

El proyecto Maven de partida está incluido en `ProyectoP4/pom.xml`: Java 17, JUnit 5, Cucumber, Mockito, JaCoCo y Jackson. Conserva `Dojo`, `Weapon`, `Money`, `Discount`, sus pruebas y el puerto heredado `DojoRepository` de P3. La raíz del agregado `Dojo` incorpora **sesiones ya programadas** representadas por `TrainingSession` y `TrainingLevel`; se proporcionan sus pruebas. Una implementación mínima `InMemoryDojoRepository` proporciona DOJO-01 (BO, KATANA y sesión SES-001, máximo 60 minutos, BEGINNER) y DOJO-02 (BO). **No se entregan casos de uso, propuestas, ejercicios ni un repositorio de propuestas**. Los detalles y comandos de inicio figuran en [BASE_INICIAL.md](docs/BASE_INICIAL.md).

**Las sesiones se dan por programadas:** P4 no crea sesiones, asigna fechas ni modifica horarios. Los datos de la sesión seleccionada se facilitan en los escenarios o mediante datos de prueba controlados; no es necesario implementar la gestión de reservas. Las nuevas clases, interfaces y relaciones deben aparecer mientras escribís pruebas y evolucionáis el código.

Podéis consultar la especificación y el código de la práctica 3 para entender los conceptos que se reutilizan. No debéis trasladar su aplicación completa a P4.

El proyecto se entregará en la carpeta `P4/` del repositorio de vuestro grupo. Las prácticas anteriores deben conservarse.

## Qué hay que desarrollar

Se desarrollarán varias historias de usuario asociadas al proyecto descrito:

### HU-01. Registrar propuestas manuales

El sensei **selecciona una sesión de entrenamiento ya programada** y registra para ella una propuesta con identificador y duración. La sesión tiene un identificador, un dojo concreto, un nivel de grupo y una duración máxima positiva en minutos. La propuesta debe quedar **asociada a la pareja de identificadores `dojoId` y `sessionId` de la sesión seleccionada**, no solo a su duración. El dojo se consulta a través de `DojoRepository` y la sesión dentro del agregado; no puede elegirse un dojo ajeno a la sesión.

Se acepta una propuesta cuando su duración es mayor que cero y no supera el tiempo disponible. Una propuesta aceptada se guarda y puede recuperarse por su identificador con su duración y la referencia completa (`dojoId` y `sessionId`) a la sesión para la que fue registrada. Una propuesta rechazada no queda registrada y se debe informar del motivo de rechazo.

Los escenarios para las pruebas de aceptación de ATDD que han sido aprobados con el *Product Owner* se encuentran detallados en el archivo donde se recogen los detalles de las historias de usuario y los contratos de aceptación.

En esta primera historia se trabajará de forma guiada, siguiendo [el laboratorio adaptado al proyecto Maven y a las sesiones programadas](04-laboratorio-hu01.md). La sesión se selecciona por identificador **dentro de un `Dojo` consultado por su ID**. El laboratorio permite aprender a abrir un escenario Cucumber, avanzar con pruebas JUnit pequeñas y revisar las responsabilidades del código una vez obtenido el resultado esperado.

### HU-02. Incorporar ejercicios técnicos y físicos a una propuesta

El sensei añade ejercicios técnicos y físicos al contenido de la propuesta que está preparando para la sesión seleccionada. Esta sesión determina el dojo, las armas disponibles, el nivel del grupo y la duración máxima. **Añadir un ejercicio no supone aceptar la propuesta:** la comprobación de reglas se realiza al solicitar su registro. No es necesario implementar almacenamiento de borradores. **El registro básico de HU-01, sin ejercicios especificados, seguirá siendo válido**. En HU-02 se presenta una propuesta detallada con una lista explícita de ejercicios que forman la propuesta; esa lista no puede estar vacía. No confundir los dos supuestos ni romper la regresión de HU-01. Una propuesta detallada puede contener varios ejercicios. Debe tener al menos uno; cada ejercicio dura un número positivo de minutos y la suma de sus duraciones coincide con la duración declarada de la propuesta.

Un ejercicio **técnico** puede requerir armas. Todas deben estar disponibles en el dojo; es válido que no requiera ninguna. Un ejercicio **físico** indica su intensidad. Para un grupo BEGINNER solo se admite LOW; para INTERMEDIATE, LOW o MEDIUM; para ADVANCED, LOW, MEDIUM o HIGH.

Los escenarios para las pruebas de aceptación de ATDD que han sido aprobados con el *Product Owner* se encuentran detallados en el archivo donde se recogen los detalles de las historias de usuario y los contratos de aceptación.

No se entrega una jerarquía de clases. Cuando las pruebas lo pidan, decidiréis qué comportamiento es común y qué reglas pertenecen a cada tipo de ejercicio. Deberéis probar las responsabilidades de los objetos y justificar las relaciones que utilicéis.

### HU-03. Evaluar propuestas sugeridas por IA

El sensei selecciona primero una sesión programada y puede solicitar una propuesta a un proveedor externo **para esa sesión**. Durante las pruebas, ese proveedor se sustituirá por respuestas controladas; no se conectará a una IA real.

La respuesta tendrá formato JSON e incluirá identificador de propuesta, duración y ejercicios. Cada ejercicio tendrá tipo, descripción y duración. Los técnicos incorporan las armas necesarias; los físicos, su intensidad. La aplicación debe comprobar que el documento es válido y aplicar las mismas reglas de negocio que a las propuestas manuales, usando las condiciones de la sesión seleccionada. Una propuesta aceptada procedente de IA queda vinculada a esa sesión, aunque el JSON del proveedor no incluya los identificadores del dojo ni de la sesión.

Los escenarios para las pruebas de aceptación de ATDD que han sido aprobados con el *Product Owner* se encuentran detallados en el archivo donde se recogen los detalles de las historias de usuario y los contratos de aceptación.

Se añadirá un tercer tipo de ejercicio: el **ejercicio combinado**. Contiene un componente técnico y otro físico. Cada componente tiene duración positiva, y la duración del combinado debe ser exactamente la suma de las dos. También se comprueban las armas y la intensidad.

El combinado añade una regla propia: **cada componente debe ocupar entre el 40 % y el 60 % de la duración total, con los límites incluidos**. Para un ejercicio de 30 minutos:

Los escenarios para las pruebas de aceptación de ATDD que han sido aprobados con el *Product Owner* se encuentran detallados en el archivo donde se recogen los detalles de las historias de usuario y los contratos de aceptación.

## Pruebas de clases, herencia y colaboración: trabajo obligatorio

**Los escenarios Cucumber no sustituyen las pruebas de los objetos.** A medida que desarrolléis las historias deberéis incorporar las **pruebas JUnit de clase, relaciones entre objetos, herencia y polimorfismo que requiera el diseño**. No se trata de escribir una prueba por cada método ni de añadir una jerarquía artificial. Se trata de comprobar las reglas y responsabilidades que aparezcan.

Revisad estos aspectos **cada vez que cerréis un comportamiento con Cucumber y durante su refactorización**:

- **Clases del dominio:** comprobar las invariantes, el estado y las reglas que corresponden a cada objeto, incluidos sus valores límite y rechazos. Una propuesta válida y otra inválida no deben depender únicamente de un test de aceptación.
- **Colaboración entre objetos:** comprobar cómo una propuesta utiliza sus ejercicios, cómo se calcula la duración total, cómo interviene el dojo y que una propuesta rechazada no se almacena. Elegid pruebas de clase o integración según la responsabilidad que queráis observar.
- **Herencia, interfaces y polimorfismo:** cuando aparezca una abstracción común, probar el **contrato que deben cumplir todos sus tipos**, las diferencias de comportamiento y el uso de objetos de distintos tipos a través de la misma abstracción. Al incorporar el ejercicio combinado en HU-03, repetir esas pruebas para comprobar que no se rompen las garantías de los ejercicios técnicos y físicos. Se admite composición bien justificada, con pruebas equivalentes; no se exige herencia decorativa.
- **Refactorización:** si trasladáis una regla desde aplicación al dominio o cambiáis una relación entre clases, conservad el comportamiento externo y comprobadlo con las pruebas existentes y las nuevas pruebas de clase que hagan falta. Una prueba de caracterización puede pasar desde el principio; no hay que inventar un RED.

Las pruebas deben surgir en el momento oportuno, no acumularse al final de la práctica. En el README identificaréis qué regla protege cada conjunto de pruebas OO, qué modificación del diseño motivó su incorporación y en qué commits se encuentra. En el vídeo explicaréis al menos una decisión de esta clase vinculada al escenario elegido.

## Suplantadores de dependencias

**En las pruebas debéis sustituir las dependencias que sea necesario controlar o aislar**, mediante un puerto o abstracción justificada por la prueba. Elegid el suplantador, cuando sea nmecesario, según **qué queréis comprobar**:

| Objetivo de la prueba | Suplantador adecuado | Qué permite observar |
|---|---|---|
| Obtener una respuesta predefinida del proveedor IA, incluido un error | **Stub** | Cómo responde el caso de uso a esos datos o fallos y el comportamiento de su estado |
| Comprobar que el caso de uso solicita guardar una propuesta, o que no lo hace tras un rechazo | **Mock** | La interacción concreta con el puerto de persistencia |
| Comprobar las reglas de duración, armas, intensidad o equilibrio | **Objeto de dominio real** | El comportamiento de negocio; **no** sustituir esta lógica con mocks |

Un mismo puerto puede utilizar dobles diferentes en pruebas con objetivos distintos. No se exige utilizar todos los tipos en todas las pruebas, pero sí demostrar que sabéis seleccionar el adecuado cuando proceda. Tampoco se debe introducir un puerto nuevo solamente para aumentar el número de mocks.

En el README incluiréis una tabla con **dependencia sustituida, prueba, objetivo, doble elegido y justificación**. Explicad también qué **no** demuestra el doble: por ejemplo, verificar una llamada a `save` no demuestra por sí solo que una propuesta pueda recuperarse. Las pruebas aisladas se complementarán con pruebas que utilicen **las reglas reales del dominio**, especialmente en la aceptación de HU-03. En el vídeo de cada participante se comentará al menos un suplantador relevante para el escenario o, si ese escenario no lo precisa, se señalará la prueba de colaboración del incremento donde se utilizó.

## Metodología de trabajo

**En parejas, el trabajo se realizará en dos etapas consecutivas, sin desarrollar ramas funcionales en paralelo.**

| Responsable | Escenarios que debe desarrollar | Condición de cierre |
|---|---|---|
|**Alumno 1 y Alumno 2**| HU-01 completa (BDD-01a–d, BDD-02, BDD-02b y BDD-03) | Se realizará conjuntamente la implementación guiada aunque los commits los realizará el **Alumno 1**. Se añadirá un último commit de fin de HU-01 |
| **Alumno 1** |  HU-02 completa (BDD-04, BDD-05 y BDD-05a–d) | Pruebas Cucumber y JUnit GREEN, PR revisado, UML HU-01/HU-02 y commit de relevo |
| **Alumno 2** | HU-03: BDD-06, BDD-07, BDD-08, BDD-09, BDD-10, BDD-11, BDD-12 y **BDD-13d** | Regresión de las tres HU GREEN, pruebas JUnit del equilibrio 40–60 %, UML HU-03 y PR final |

La HU-01 tiene una guía común, pero el **alumno 1 es responsable de completar y dejar integrada la etapa 1 y 2**. El alumno 2 revisa el PR y empieza desde el commit GREEN de relevo; no debe anticipar ni intervenir continuamente en decisiones del diseño del alumno 1 sobre HU-02. El alumno 1 revisa el trabajo final del alumno 2. Si trabaja una sola persona, realizará ambas etapas por orden.

Cada alumno grabará un vídeo de un escenario significativo **de su propia etapa**: alumno 1, de HU-02; alumno 2, de HU-03, preferentemente el nuevo rechazo por desequilibrio de BDD-13d. Las decisiones de pruebas orientadas a objetos y de suplantadores se evaluarán en relación con las responsabilidades que cada alumno haya desarrollado.

En cada incremento seguiréis este procedimiento:

1. Escribid o incorporad el escenario de aceptación acordado usando **Gherkin en inglés**. Ejecutadlo con Cucumber y registrad su estado inicial.
2. Desarrollad la funcionalidad mediante **tests JUnit escritos antes del código de producción**. Avanzad con ciclos RED, GREEN y REFACTOR pequeños y verificables.
3. Cuando el escenario sea GREEN, revisad el diseño: trasladad al dominio las reglas que le corresponden y añadid las pruebas JUnit de clase, colaboración, contratos de herencia y polimorfismo que sean necesarias para proteger las responsabilidades y relaciones que hayan surgido.
4. En cada prueba que dependa de almacenamiento o del proveedor externo, decidid si necesitáis un stub, mock, fake o una implementación real según el resultado que queréis observar. Justificadlo y ejecutad la regresión completa.
5. Comprobad las métricas de calidad, estudiad los hallazgos relevantes y justificad los cambios que decidáis realizar.

Una prueba Cucumber sin pasos implementados puede quedar `undefined` o `pending`; eso no es un RED de una regla de negocio. Un `Then` vacío tampoco equivale a una aceptación válida. Un fallo de compilación o de infraestructura no debe confundirse con una prueba funcional que falla.

La refactorización cambia el diseño sin cambiar el comportamiento. Si encontráis una regla nueva, primero debéis escribir una prueba que la exija. No hay que provocar artificialmente fallos ni inventar commits para aparentar que se siguió TDD.

Podéis utilizar asistentes de IA como ayuda. Debéis revisar el código que incorporéis y ser capaces de explicar las pruebas, las decisiones de diseño y los cambios realizados. El historial de Git y los vídeos permitirán comprobar el proceso.

## Auditoría del trabajo

### GitHub y commits

Los commits deberán seguir la **convención obligatoria en español** `TIPO HU-XX BDD-YY: descripción concreta`. Se distinguen los siguientes tipos: `ATDD-TEST` (especificación/feature), `ATDD-FEAT` (automatización de los pasos), `TDD-TEST` (prueba JUnit antes del código), `TDD-FEAT` (implementación mínima) y `TDD-REFACTOR` (mejora del diseño con comportamiento conservado). En todo commit de este tipo debe constar **la HU y el escenario BDD** incluso en las pruebas internas y en las refactorizaciones; indicad todos los escenarios afectados si hay más de uno.

Ejemplos: `ATDD-TEST HU-02 BDD-05b: especificar rechazo por suma incorrecta`; `TDD-TEST HU-02 BDD-05b: exigir suma coherente`; `TDD-FEAT HU-02 BDD-05b: validar suma de duraciones`; `TDD-REFACTOR HU-02 BDD-05a+BDD-05b: trasladar regla al dominio`; `ATDD-FEAT HU-02 BDD-05b: verificar el rechazo mediante Cucumber`. Todos son orientativos, no commits que debáis simular.

El `README_P4.md` explicará **cómo y cuándo** se hicieron los commits, cómo se ejecutó cada hito y cómo reconstruir un ciclo real con hashes y PR. Debéis crear un commit `TDD-TEST` después de observar el RED genuino, antes del código de producción; otro `TDD-FEAT` tras comprobar GREEN; y `TDD-REFACTOR` si hubo refactorización real, con regresión GREEN. Un `undefined/pending` de Cucumber es un estado ATDD inicial, no un RED unitario. No hay número fijo de commits ni hay que fabricar errores. Consultad [el laboratorio HU-01](04-laboratorio-hu01.md) y [la guía de auditoría](08-guia-auditoria-uml-video.md).

Conservad los estados reales de trabajo necesarios para la auditoría. Evitad combinar o reescribir los commits que vais a usar como evidencia. No hagáis un merge de una rama que todavía tiene pruebas deliberadamente rojas. Los PR finales deben quedar verificados.

### Diagramas UML

Entregaréis **tres diagramas UML de clases**, correspondientes a estados reales del repositorio: después de HU-01, después de HU-02 y después de HU-03. Cada uno llevará el hash del commit al que pertenece.

Mostrad las clases, las responsabilidades principales y las relaciones pertinentes. Si aparece una interfaz, una jerarquía o una composición, representadla correctamente. Explicad qué test o nueva regla justificó cada cambio. Podéis utilizar Mermaid, PlantUML u otra herramienta que permita consultar el diagrama y su fuente.

En el vídeo compararéis al menos dos estados del diseño del escenario elegido. No dibujéis retrospectivamente un diseño que todavía no existía en el commit indicado.

### Vídeo individual

Cada estudiante entregará un vídeo de **hasta 6–7 minutos**, enlazado desde el README. Debe mostrar un escenario significativo de la etapa asignada: HU-02 para el alumno 1 y HU-03 para el alumno 2. En el vídeo se explicarán el acuerdo de aceptación, el estado inicial de Cucumber, los tests JUnit RED y GREEN, la refactorización, las pruebas de clase y de herencia o polimorfismo que correspondan al diseño, y las pruebas de colaboración con suplantadores justificados, el resultado final de Cucumber y la regresión.

Debéis navegar por **commits reales** y mostrar sus diferencias. Reservad la parte final para examinar los resultados de GitHub Actions, JaCoCo y SonarQube. Elegid uno o dos hallazgos que merezcan una explicación: qué significan, qué riesgo representan, qué habéis hecho o por qué no era necesario modificar el código y cómo lo habéis comprobado.

Grabación de pantalla con la voz del estudiante al mismo tiempo que maneja las herramientas. No es necesario mostrar la cámara. Se permiten transiciones entre secciones, pero no una narración en voz en off grabada por separado. El cursor, el texto y el audio deben ser claros. El vídeo se alojará fuera del repositorio, por ejemplo en YouTube como no listado, y deberá poder reproducirse desde el enlace entregado.

### Análisis de calidad

La entrega debe permitir ejecutar `mvn -f P4/pom.xml clean verify`, comprobar la ejecución de JUnit y Cucumber y consultar los resultados de GitHub Actions y de SonarQube institucional con el Quality Gate CalSo. Analizad la cobertura de JaCoCo, las incidencias relevantes, la complejidad o la duplicación cuando se hayan medido y las consecuencias de los cambios realizados.

No basta con escribir que se ha obtenido un porcentaje elevado de cobertura o que el Quality Gate está en verde. Explicad qué se ha medido, qué queda sin comprobar y qué decisión tomáis a partir del resultado.

## Entrega

Todo el código, las pruebas y la documentación irán en `ProyectoP4/` de vuestro repositorio. El workflow de GitHub Actions se ubicará en `.github/workflows/` de la raíz del repositorio. Completad la plantilla `README_P4.md` que se os facilita: incluye planificación, reglas, pruebas, política de commits, diagramas UML, vídeos y análisis final. No añadáis documentos extensos para repetir información ya enlazada.

La evaluación seguirá la **rúbrica analítica de 100 puntos** publicada con la práctica. Se tendrá en cuenta que la aplicación funcione, pero también la calidad de los escenarios, el uso real de TDD, las pruebas de clases, colaboración, herencia y polimorfismo pertinentes, el traslado de reglas al dominio, la selección justificada de stubs, mocks y fakes, la interpretación de las métricas y la posibilidad de auditar el desarrollo con Git.
