# 01 · Contexto y Product Backlog de P4

## Producto y estado de partida

Se incluye una **base Maven pequeña** en `ProyectoP4/`, con el modelo heredado `Dojo`, `Weapon`, `Money` y `Discount`, sus pruebas, el puerto `DojoRepository` procedente de P3 y una implementación mínima en memoria que aporta DOJO-01/DOJO-02. `Dojo` se amplía como **raíz del agregado** que contiene las sesiones programadas; se entregan `TrainingSession` y `TrainingLevel`, incluida `SES-001` en `DOJO-01`. El alumnado puede consultar P3, pero no debe copiar su aplicación completa. No se proporcionan casos de uso, repositorios de propuestas, ejercicios ni clientes IA; **las sesiones programadas sí se proporcionan** y no forman parte de las tareas de desarrollo.

- **Actor:** entrenador responsable de la sesión.
- **Product Owner:** representante del servicio municipal.
- **Equipo de desarrollo:** alumnado organizado en grupos de hasta dos participantes.

**Fuera de alcance:** crear, programar, modificar o reservar sesiones; gestionar calendario, fechas u horarios; interfaces gráficas, autenticación, base de datos real, integración con un proveedor de IA en producción, pagos y las funcionalidades de reserva de P3. Las sesiones se dan por existentes y sus datos se aportan como contexto del caso de uso o datos de prueba controlados.

Las tres historias se formulan como tarjetas de producto: **Como [rol], quiero [capacidad], para [beneficio]**. Sus reglas y criterios de aceptación se concretan en [02-aceptacion.md](02-aceptacion.md). Ni los nombres de clases ni el uso de Cucumber, mocks o herencia forman parte de lo solicitado por el entrenador: son decisiones y obligaciones técnicas de la práctica, descritas en el enunciado de la práctica.

## Vocabulario compartido

| Término | Significado |
|---|---|
| Sesión de entrenamiento | Sesión **ya programada**, identificada por ID, asociada a un dojo y con duración máxima positiva y nivel del grupo; P4 la utiliza, pero no la registra ni gestiona su calendario |
| Dojo | Lugar donde están disponibles determinadas armas |
| Propuesta | Plan identificado por ID, asociado a **una sesión concreta** y con duración total; si se acepta, queda registrado y es recuperable junto con esa vinculación |
| Ejercicio técnico | Actividad con duración y conjunto de armas requeridas, que puede estar vacío |
| Ejercicio físico | Actividad con duración e intensidad LOW, MEDIUM o HIGH |
| Ejercicio combinado | Actividad con un componente técnico y uno físico, cada uno con duración positiva; aplica las reglas de ambos y exige equilibrio temporal |
| Propuesta sugerida por IA | Propuesta descrita en un JSON recibido de un servicio externo |
| Rechazo | Resultado que indica la causa y no registra la propuesta |

## HU-01 · Registrar y consultar propuestas manuales

**Prioridad:** 1. **Desarrollo:** laboratorio guiado.

> **Como** entrenador que prepara una sesión, **quiero** registrar una propuesta manual para una sesión programada que he seleccionado y consultarla después, **para** conservar únicamente las propuestas que se ajustan a las condiciones de esa sesión.

### Conversación con el Product Owner

El sensei selecciona la sesión previamente programada por el dojo y aporta identificador y duración de la propuesta. 

Cada sesión tiene identificador, dojo, nivel del grupo y duración máxima positiva en minutos. La propuesta queda vinculada a la **pareja `dojoId`/`sessionId`** del agregado de destino. 

El dojo se recupera por `DojoRepository` y la sesión se obtiene a través de `Dojo.findSessionById`; no son selecciones independientes. 

La duración admisible es un número entero comprendido entre 1 y el tiempo disponible, ambos extremos incluidos. 

Si el registro se acepta, la propuesta se guarda y se recupera por ID con los mismos datos **y la misma pareja dojo/sesión de destino**. Si se rechaza, se comunica la causa y no se guarda.

### Criterios de aceptación

- Una propuesta de duración positiva que no supera la duración máxima de **la sesión seleccionada** se acepta y se recupera vinculada a ella.
- Una duración superior al tiempo disponible se rechaza con `PROPOSAL_EXCEEDS_AVAILABLE_DURATION`.
- Una duración de cero minutos se rechaza con `INVALID_PROPOSAL_DURATION`.
- Ninguna propuesta rechazada aparece en la consulta de propuestas registradas.

**Ejemplos:** BDD-01a/b/c/d, BDD-02/02b y BDD-03 de 02. HU-01 trabaja con **propuestas básicas, sin lista de ejercicios especificada**, que deben seguir siendo válidas tras HU-02. HU-02 introduce las **propuestas detalladas**, con lista explícita de ejercicios. Una lista detallada vacía se rechaza; no debe confundirse con el registro básico ya aceptado en HU-01.

## HU-02 · Incorporar ejercicios técnicos y físicos a una propuesta

**Prioridad:** 2. **Desarrollo:** autónomo, después del registro básico.

> **Como** sensei responsable de preparar una sesión de entrenamiento, **quiero** añadir ejercicios técnicos y físicos a una propuesta de entrenamiento, **para** definir las actividades que realizarán los participantes durante la sesión.

### Conversación con el Product Owner

El sensei selecciona la sesión programada y prepara el contenido de la propuesta incorporando ejercicios técnicos y físicos. La propuesta detallada contiene uno o varios ejercicios. Sus duraciones son positivas y deben sumar la duración total declarada. Los ejercicios técnicos pueden necesitar armas; todas deben estar disponibles. Los físicos expresan intensidad. El nivel BEGINNER solo admite LOW; INTERMEDIATE admite LOW y MEDIUM; ADVANCED admite LOW, MEDIUM y HIGH.

Añadir ejercicios forma parte de preparar la propuesta; **no implica aceptarla ni obliga a guardar borradores**. Al solicitar su registro se validan los ejercicios. Una propuesta con un ejercicio técnico y otro físico debe poder registrarse si cumple todas las reglas. Las que incumplen una condición se rechazan sin quedar registradas.

### Criterios de aceptación

- Cada propuesta detallada contiene al menos un ejercicio con duración positiva.
- La duración declarada coincide con la suma de las duraciones de los ejercicios.
- Todos los requisitos de armas de los ejercicios técnicos se satisfacen en el dojo.
- La intensidad de los ejercicios físicos es admisible para el nivel del grupo.
- Las propuestas válidas, incluso si mezclan tipos de ejercicios, se aceptan y recuperan; las inválidas no se registran.

**Ejemplos:** BDD-04 (arma no disponible), BDD-05 (intensidad incompatible), BDD-05a (propuesta mixta válida), BDD-05b (suma incoherente), BDD-05c (propuesta detallada con lista expresamente vacía) y BDD-05d (ejercicio con duración nula). El contrato cierra también BDD-05c (lista vacía: `EXERCISES_REQUIRED`) y BDD-05d (ejercicio de duración cero: `INVALID_EXERCISE_DURATION`), además de BDD-05b (`EXERCISE_DURATION_MISMATCH`). Los códigos son comunes para todos los grupos y no requieren negociación. La duración global se valida antes de la lista, las duraciones individuales y la suma.

## HU-03 · Solicitar y evaluar propuestas sugeridas por IA

**Prioridad:** 3. **Desarrollo:** autónomo, con dependencia de las reglas de HU-01 y HU-02.

> **Como** entrenador que busca nuevas opciones para una sesión, **quiero** solicitar propuestas a un asistente de IA para una sesión programada y comprobar su viabilidad, incluidos los ejercicios que combinan trabajo técnico y físico, **para** aprovechar sus sugerencias sin incorporar entrenamientos inviables o desequilibrados.

### Conversación con el Product Owner

El sensei selecciona una sesión antes de solicitar la propuesta. El asistente envía una respuesta JSON con identificador, duración y ejercicios. Si se acepta, la propuesta se asocia a la pareja dojo/sesión seleccionada, cuyos IDs no tienen por qué figurar en el JSON del proveedor. El sistema debe detectar una respuesta inválida o un fallo del proveedor. Una respuesta válida no se registra automáticamente: debe cumplir las mismas reglas que una propuesta manual.

El proveedor puede incluir un tercer tipo de ejercicio, **combinado**, formado por un componente técnico y uno físico. Sus duraciones son positivas y suman la del combinado. Deben cumplirse las reglas de armas e intensidad. Además, **cada componente debe representar al menos el 40 % y como máximo el 60 % de la duración total del combinado**. Los límites están incluidos. Esta condición corresponde al contenido del entrenamiento, no al formato JSON ni al proveedor.

### Criterios de aceptación

- Un JSON válido que describe una propuesta viable se acepta, registra y puede recuperarse.
- Si excede los minutos disponibles, se rechaza con `PROPOSAL_EXCEEDS_AVAILABLE_DURATION`, incluso cuando también haya una intensidad no admitida.
- Un documento JSON malformado o incompatible con el contrato se rechaza con `EXTERNAL_RESPONSE_INVALID`; la falta de respuesta del proveedor se comunica con `AI_SERVICE_UNAVAILABLE`.
- El ejercicio combinado cumple las restricciones técnicas y físicas de sus componentes y la regla de equilibrio temporal 40–60 %.
- Un combinado desequilibrado se rechaza sin registro. Se utiliza el código confirmado `COMBINED_EXERCISE_UNBALANCED`.
- Los ejercicios técnicos, físicos y combinados pueden coexistir sin cambiar las reglas ya acordadas para los primeros.
- Ninguna propuesta rechazada se almacena.

**Aceptación Cucumber obligatoria:** BDD-06 a BDD-09, BDD-10 a BDD-12 y BDD-13d. **Pruebas JUnit de dominio obligatorias:** los valores 15/15, 12/18, 18/12, 11/19 y 19/11, incluidos los ejemplos complementarios BDD-13a/b/c/e. BDD-13a repite la aceptación válida BDD-10; no hace falta otro escenario de aceptación. El JSON de referencia está en 02.

## Dependencias, planificación y distribución del trabajo

```text
HU-01: registro manual y motivos de rechazo
              |
              v
HU-02: contenido técnico/físico y validación
              |
              v
HU-03: propuestas IA, combinado y equilibrio 40–60 %
```

Las dependencias son deliberadas. **En parejas, el alumno 1 desarrolla por orden HU-01 y HU-02 hasta BDD-05d; el alumno 2 continúa HU-03 desde BDD-06 hasta BDD-13d.** El primer alumno integra su trabajo mediante PR y deja la regresión GREEN, con commit y UML de relevo; el segundo revisa ese PR y solo entonces comienza. Al terminar HU-03, el primero revisa el PR final. No se exige desarrollo funcional simultáneo ni alternancia continua de ramas. Los dos alumnos deben poder demostrar individualmente un ciclo real ATDD/TDD, el primero en HU-02 y el segundo en HU-03. Una entrega individual desarrolla todas las etapas en el mismo orden.

| Etapa | Responsable | Escenarios obligatorios | Evidencia de cierre |
|---|---|---|---|
| 1 | Alumno 1 y 2 | BDD-01a–d, 02/02b y 03 | HU-01 integrada y GREEN; UML tras HU-01 y commit de fin de HU-01 |
| 1 | Alumno 1 | BDD-04, 05, 05a–d | HU-02 integrada y GREEN; UML tras HU-02, PR y commit de relevo |
| 2 | Alumno 2 | BDD-06–09, 10–12 y 13d | HU-03 integrada y GREEN; JUnit de límites 40–60 %, UML final y PR revisado |

## Acuerdos cerrados y límites del backlog

Los casos de uso y resultados detallados están recogidos en 02. Ya quedan confirmados el código `COMBINED_EXERCISE_UNBALANCED`, el esquema JSON COMBINED con `technical` y `physical`, las respuestas exactas de BDD-08 y BDD-09 y la precedencia **duración global → lista no vacía → duración individual positiva → suma → equilibrio → intensidad → armas**. La respuesta `{"error": "Invalid access"}` de BDD-09 representa error del proveedor y se traduce a `AI_SERVICE_UNAVAILABLE`, mientras que BDD-08 utiliza un JSON malformado y se traduce a `EXTERNAL_RESPONSE_INVALID`. IDs duplicados, sesiones no positivas, campos JSON desconocidos y reintentos del proveedor quedan expresamente fuera de alcance. Los grupos no negociarán estos extremos.

## Criterio de calidad del desarrollo (no forma parte de las tarjetas de usuario)

El enunciado exige automatizar aceptación con Cucumber; construir funcionalidades con microciclos JUnit RED–GREEN–REFACTOR; identificar y trasladar reglas de negocio al dominio; incorporar pruebas de clase, colaboración, contratos de herencia y polimorfismo cuando lo justifique el diseño; y escoger stubs, mocks o fakes por objetivo de prueba. El historial real de Git, el UML progresivo, el vídeo individual y el análisis de JaCoCo/SonarQube permitirán comprobar el proceso. Véanse [guía](08-guia-auditoria-uml-video.md), [plantilla de entrega](PLANTILLA_README_P4.md) y [rúbrica](07-rubrica-analitica.md).


**Trazabilidad obligatoria del trabajo:** los commits de las tres historias siguen la convención en español `TIPO HU-XX BDD-YY: descripción`, con las fases `ATDD-TEST`, `ATDD-FEAT`, `TDD-TEST`, `TDD-FEAT` y `TDD-REFACTOR`. El identificador BDD acompaña también a los microciclos JUnit y las refactorizaciones; el [laboratorio HU-01](04-laboratorio-hu01.md) contiene la secuencia y [la guía de auditoría](08-guia-auditoria-uml-video.md) el procedimiento de revisión.
