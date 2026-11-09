# 02 · Historias de usuario y contrato de aceptación de P4

## 1. HU-01 · Registrar y consultar propuestas manuales

> **Como** sensei que prepara una sesión, **quiero** registrar una propuesta manual para una sesión programada que he seleccionado y consultarla después, **para** conservar únicamente las propuestas que se ajustan a las condiciones de esa sesión.

### Criterios de aceptación

**CA-01.1.** Al seleccionar una sesión existente dentro de su dojo, la propuesta de duración entre 1 y la duración máxima de esa sesión, ambos incluidos, se acepta, se registra y se recupera con sus datos **y ambos IDs de destino: `dojoId` y `sessionId`**.

**CA-01.2.** Si la duración supera la duración máxima de la sesión seleccionada, se rechaza con `PROPOSAL_EXCEEDS_AVAILABLE_DURATION` y no queda registrada.

**CA-01.3.** Si la duración es cero, se rechaza con `INVALID_PROPOSAL_DURATION` y no queda registrada.

| ID | Dado | Cuando | Entonces | Estado |
|---|---|---|---|---|
| BDD-01a | Sesión **SES-001** (DOJO-01, máximo 60 min); PROP-001 duración 45 | Registrar PROP-001 para **SES-001** | Aceptada; recuperable con duración 45 y `dojoId = DOJO-01` y `sessionId = SES-001` | Confirmado |
| BDD-01b | Sesión **SES-001** (DOJO-01, máximo 60 min); PROP-004 duración 59 | Registrar PROP-004 para **SES-001** | Aceptada; recuperable con duración 59 y `dojoId = DOJO-01` y `sessionId = SES-001` | Confirmado |
| BDD-01c | Sesión **SES-001** (DOJO-01, máximo 60 min); PROP-005 duración 60 | Registrar PROP-005 para **SES-001** | Aceptada; recuperable con duración 60 y `dojoId = DOJO-01` y `sessionId = SES-001` | Confirmado |
| BDD-01d | Sesión **SES-001** (DOJO-01, máximo 60 min); PROP-006 duración 1 | Registrar PROP-006 para **SES-001** | Aceptada; recuperable con duración 1 y `dojoId = DOJO-01` y `sessionId = SES-001` | Confirmado |
| BDD-02 | Sesión **SES-001** (DOJO-01, máximo 60 min); PROP-002 duración 75 | Registrar PROP-002 para **SES-001** | Rechazada `PROPOSAL_EXCEEDS_AVAILABLE_DURATION`; PROP-002 no registrada para SES-001 | Confirmado |
| BDD-02b | Sesión **SES-001** (DOJO-01, máximo 60 min); PROP-007 duración 61 | Registrar PROP-007 para **SES-001** | Rechazada `PROPOSAL_EXCEEDS_AVAILABLE_DURATION`; PROP-007 no registrada para SES-001 | Confirmado |
| BDD-03 | Sesión **SES-001** (DOJO-01, máximo 60 min); PROP-003 duración 0 | Registrar PROP-003 para **SES-001** | Rechazada `INVALID_PROPOSAL_DURATION`; PROP-003 no registrada para SES-001 | Confirmado |

**Comprobación común a BDD-01a–d:** el objeto registrado recuperado debe conservar `dojoId = DOJO-01` y `sessionId = SES-001`; no basta con comprobar la duración o el booleano de aceptación. En los rechazos debe comprobarse ausencia de registro para esa propuesta.

**Compatibilidad entre HU-01 y HU-02:** el contrato de HU-01 describe el **registro básico sin ejercicios especificados**, que permanece válido como comportamiento anterior. HU-02 introduce el **registro de propuestas detalladas**; cuando se presenta una lista de ejercicios como contenido de la propuesta, esa lista debe tener al menos un elemento. BDD-05c representa una propuesta detallada cuya lista está expresamente vacía y debe rechazarse. El sistema ha de distinguir funcionalmente esos dos supuestos, sin que este contrato imponga cómo hacerlo mediante clases, parámetros o sobrecargas Java. Las pruebas de regresión de HU-01 deben seguir en GREEN tras completar HU-02.

Los casos 1/60 y 0/60 comprueban el mínimo; 60/60 y 61/60 el máximo. El caso 75/60 representa la partición de exceso, no el valor límite inmediato. HU-01 trabaja con registro manual sin ejercicios detallados.

## 2. HU-02 · Incorporar ejercicios técnicos y físicos a una propuesta

> **Como** sensei responsable de preparar una sesión de entrenamiento, **quiero** añadir ejercicios técnicos y físicos a una propuesta de entrenamiento, **para** definir las actividades que realizarán los participantes durante la sesión.

La capacidad solicitada consiste en **incorporar ejercicios al contenido de una propuesta manual destinada a una sesión seleccionada**. Los criterios siguientes determinan qué condiciones debe cumplir ese contenido cuando se solicita el registro. No se exige un servicio de edición de borradores ni persistencia previa a la aceptación.

### Criterios de aceptación

**CA-02.1.** La propuesta **detallada**, para la que se ha especificado una lista de ejercicios, debe contener al menos un ejercicio. Si la lista está vacía, se rechaza con `EXERCISES_REQUIRED`.

**CA-02.1a.** Cada ejercicio debe tener duración individual mayor que cero. Si alguno tiene duración cero o negativa, se rechaza con `INVALID_EXERCISE_DURATION`.

**CA-02.1b.** La suma de las duraciones individuales debe coincidir con la duración declarada de la propuesta. Si no coincide, se rechaza con `EXERCISE_DURATION_MISMATCH`.

**CA-02.1c. Orden de validación acordado para HU-02:** (1) duración global de propuesta respecto de la sesión y validez de la duración global; (2) lista de ejercicios no vacía; (3) duración positiva de todos los ejercicios; (4) suma de duraciones; (5) restricciones de armas e intensidad. Ante incumplimientos simultáneos de los puntos 1–4 se comunica el primer motivo de esta secuencia. En el punto 5 se comprueba **equilibrio → intensidad → armas**. Cuando concurran infracciones se devuelve el primer motivo en ese orden; no se exige añadir escenarios Cucumber combinados para probar esta prioridad, pero sí tests JUnit que la protejan.

**CA-02.2.** Se admite un ejercicio técnico cuando todas las armas requeridas están disponibles en el dojo; el conjunto requerido puede estar vacío.

**CA-02.3.** La intensidad física permitida es LOW para BEGINNER; LOW/MEDIUM para INTERMEDIATE; LOW/MEDIUM/HIGH para ADVANCED.

**CA-02.4.** La propuesta se evalúa usando el dojo, las armas, el nivel y la duración máxima **de su sesión de destino**; no valores suministrados por la propuesta para sustituir la sesión. Una propuesta puede combinar ejercicios técnicos y físicos. Si todos cumplen las reglas, se registra y recupera; si incumple alguna, se rechaza sin almacenar.

| ID | Dado | Cuando | Entonces | Estado |
|---|---|---|---|---|
| BDD-04 | **DOJO-01**, sesión **SES-001** (máximo 60 min); PROP-TEC-004 de 45 con TECHNICAL 45 que requiere NUNCHAKU; el dojo dispone de BO y KATANA | Registrar la propuesta para **SES-001** | Rechazada `REQUIRED_WEAPON_UNAVAILABLE`; no registrada para SES-001 | Confirmado |
| BDD-05 | **DOJO-01**, sesión **SES-001** (BEGINNER, máximo 60 min); PROP-FIS-005 de 45 con PHYSICAL 45 HIGH | Registrar la propuesta para **SES-001** | Rechazada `INTENSITY_NOT_ALLOWED`; no registrada para SES-001 | Confirmado |
| BDD-05a | **DOJO-01** (dispone de BO), sesión **SES-001** (BEGINNER, máximo 60 min); PROP-MIX-005A de 45: TECHNICAL 25 BO y PHYSICAL 20 LOW | Registrar la propuesta para **SES-001** | Aceptada, recuperable con ambos ejercicios, duración 45 y `dojoId = DOJO-01` y `sessionId = SES-001` | Confirmado para P4 |
| BDD-05b | **DOJO-01** (dispone de BO), sesión **SES-001** (BEGINNER, máximo 60 min); PROP-MIX-005B declara 50, con TECHNICAL 25 BO y PHYSICAL 20 LOW | Registrar la propuesta para **SES-001** | Rechazada `EXERCISE_DURATION_MISMATCH`; no registrada para SES-001 | Acuerdo cerrado para P4 |
| BDD-05c | **DOJO-01** (BO disponible), sesión **SES-001** (BEGINNER, máximo 60 min); PROP-MIX-005C declara 45 minutos y tiene una **lista vacía de ejercicios** | Registrar PROP-MIX-005C para **SES-001** | Rechazada `EXERCISES_REQUIRED`; no registrada para SES-001 | Acuerdo cerrado para P4 |
| BDD-05d | **DOJO-01** (BO disponible), sesión **SES-001** (BEGINNER, máximo 60 min); PROP-MIX-005D declara 45 minutos; TECHNICAL 0 minutos con BO y PHYSICAL 45 minutos LOW | Registrar PROP-MIX-005D para **SES-001** | Rechazada `INVALID_EXERCISE_DURATION`; no registrada para SES-001 | Acuerdo cerrado para P4 |

**Por qué se han elegido estos datos:** BDD-05c hace fallar únicamente la exigencia de que exista algún ejercicio. BDD-05d conserva la suma total (0 + 45 = 45), por lo que su único incumplimiento es la duración individual nula. BDD-05b comprueba una suma incorrecta sin otras infracciones. Para una duración individual negativa se exige una prueba JUnit equivalente, sin crear otro escenario de aceptación obligatorio. Estos motivos son definitivos para todos los grupos; **no es necesario consultar al docente**.

## 3. HU-03 · Solicitar y evaluar propuestas sugeridas por IA

> **Como** sensei que busca nuevas opciones para una sesión, **quiero** solicitar propuestas a un asistente de IA para una sesión programada y comprobar su viabilidad, incluidos los ejercicios que combinan trabajo técnico y físico, **para** aprovechar sus sugerencias sin incorporar entrenamientos inviables o desequilibrados.

### Criterios de aceptación

**CA-03.1.** El proveedor devuelve JSON con `proposalId`, `durationMinutes` y `exercises`. Cada ejercicio lleva `type`, `description` y `durationMinutes`. Los tipos TECHNICAL y PHYSICAL utilizan respectivamente `requiredWeapons` (array) e `intensity` (LOW/MEDIUM/HIGH).

**CA-03.2.** El sensei selecciona primero **SES-001**, perteneciente a **DOJO-01** en los ejemplos. Una respuesta bien formada que cumple las reglas de duración y ejercicios **de esa sesión** se registra y recupera con **`dojoId` y `sessionId`** iguales a los del destino seleccionado, aunque el JSON externo no incluya dichos campos. Una respuesta inválida se rechaza con `EXTERNAL_RESPONSE_INVALID`; si el proveedor no está disponible se responde `AI_SERVICE_UNAVAILABLE`. Ninguna respuesta rechazada produce un registro.

**CA-03.3.** Si la propuesta excede los minutos disponibles, el motivo externo es `PROPOSAL_EXCEEDS_AVAILABLE_DURATION`, aunque la intensidad también sea incorrecta. Una vez superadas las validaciones estructurales, los errores especializados se priorizan en el orden **equilibrio, intensidad y armas**.

**CA-03.4.** Se admite el tipo **COMBINED**: un ejercicio compuesto por una parte técnica y otra física, ambas con duración positiva. La duración del combinado es la suma de ambas y cada parte cumple su regla de armas o intensidad.

**CA-03.5. Equilibrio propio del combinado:** cada componente debe ocupar **al menos el 40 % y como máximo el 60 %** del tiempo del ejercicio, con ambos límites incluidos. Con duraciones enteras puede comprobarse sin redondeos: `5 * componentMinutes >= 2 * combinedMinutes` para ambos componentes. La regla se aplica al entrenamiento, no al formato de su JSON. Un ejercicio desequilibrado se rechaza y no se registra.

**CA-03.6.** La incorporación de COMBINED no altera la validez de ejercicios TECHNICAL y PHYSICAL ya admitidos.

### Aceptación de respuesta y errores externos

| ID | Dado | Cuando | Entonces | Estado |
|---|---|---|---|---|
| BDD-06 | **DOJO-01** (dispone de BO), sesión **SES-001** (BEGINNER, máximo 60 min); IA devuelve PROP-IA-001 de 45 con TECHNICAL 25 BO y PHYSICAL 20 LOW | Solicitar propuesta IA para **SES-001** | Aceptada y recuperable con ambos ejercicios y `dojoId = DOJO-01` y `sessionId = SES-001` | Confirmado |
| BDD-07 | **DOJO-01**, sesión **SES-001** (BEGINNER, máximo 60 min); IA devuelve PROP-IA-002 de 75 con PHYSICAL 75 HIGH | Solicitar propuesta IA para **SES-001** | Rechazada `PROPOSAL_EXCEEDS_AVAILABLE_DURATION`; no registrada para SES-001 | Confirmado |
| BDD-08 | **DOJO-01**, sesión **SES-001** (BEGINNER, máximo 60 min); el proveedor devuelve el **JSON malformado literal BDD-08** indicado abajo (falta la propiedad `exercises` antes del objeto COMBINED) | Solicitar propuesta IA para **SES-001** | Rechazada `EXTERNAL_RESPONSE_INVALID`; ninguna propuesta registrada para SES-001 | Confirmado; payload cerrado |
| BDD-09 | **DOJO-01**, sesión **SES-001** (BEGINNER, máximo 60 min); el proveedor devuelve `{"error": "Invalid access"}` en lugar de una propuesta | Solicitar propuesta IA para **SES-001** | Rechazada `AI_SERVICE_UNAVAILABLE`; ninguna propuesta registrada para SES-001 | Confirmado; respuesta de error cerrada |

### Respuestas exactas del proveedor para BDD-08 y BDD-09

**BDD-08 — JSON malformado, que debe conservarse incorrecto:** falta el nombre de la propiedad `exercises` y aparece directamente un objeto después de `durationMinutes`. La respuesta exacta es:

```json
{
  "proposalId": "PROP-IA-010",
  "durationMinutes": 30,
  {
    "type": "COMBINED",
    "description": "Basic mixed training",
    "durationMinutes": 30,
    "technical": {
      "description": "BO basics",
      "durationMinutes": 15,
      "requiredWeapons": ["BO"]
    },
    "physical": {
      "description": "Mobility",
      "durationMinutes": 15,
      "intensity": "LOW"
    }
  }
}
```

Se rechaza con `EXTERNAL_RESPONSE_INVALID`; no se repara ni completa silenciosamente el JSON. Aunque contenga datos semejantes a un combinado válido, no es un documento JSON analizable.

**BDD-09 — error de acceso comunicado por el proveedor:**

```json
{"error": "Invalid access"}
```

Se considera una **respuesta de error del servicio externo**, no una propuesta JSON incompleta. La aplicación traduce esa respuesta a `AI_SERVICE_UNAVAILABLE` y no registra nada. Los tests deben diferenciar expresamente este mensaje de error del documento malformado de BDD-08; no se necesita llamar a un servicio real.

### Aceptación de ejercicios combinados

**Contexto expreso de todos los escenarios BDD-10 a BDD-12:** dojo **DOJO-01** (dispone de BO), sesión **SES-001** (duración máxima 60 minutos, BEGINNER), seleccionada por el sensei. El proveedor devuelve una propuesta de 30 minutos con un solo COMBINED para **SES-001**. El combinado tiene una parte técnica que requiere BO y otra física LOW, **salvo el único atributo que cambia cada ejemplo**.

| ID | Técnico | Físico | Variación | Entonces | Estado |
|---|---:|---:|---|---|---|
| BDD-10 | 15 | 15 | Ninguna | Aceptada, registrada y recuperable con duración total 30 | Confirmado para P4 |
| BDD-11 | 15 | 15 | Técnico requiere NUNCHAKU no disponible | Rechazada `REQUIRED_WEAPON_UNAVAILABLE`; no registrada | Confirmado para P4 |
| BDD-12 | 15 | 15 | Físico HIGH para BEGINNER | Rechazada `INTENSITY_NOT_ALLOWED`; no registrada | Confirmado para P4 |

### Aceptación del equilibrio 40–60 %

**Contexto expreso de BDD-13a a BDD-13e:** dojo **DOJO-01** (dispone de BO), sesión seleccionada **SES-001** (BEGINNER, duración máxima 60 minutos); propuesta IA de 30 minutos con un único COMBINED. Únicamente cambian los minutos de los componentes.

| ID | Técnico | Físico | Entonces | Estado |
|---|---:|---:|---|---|
| BDD-13a | 15 | 15 | Aceptada y recuperable | **JUnit obligatorio**; mismo positivo que BDD-10 |
| BDD-13b | 12 | 18 | Aceptada y recuperable; límite 40/60 | **JUnit obligatorio** |
| BDD-13c | 18 | 12 | Aceptada y recuperable; límite 60/40 | **JUnit obligatorio** |
| BDD-13d | 11 | 19 | Rechazada, no registrada; motivo confirmado `COMBINED_EXERCISE_UNBALANCED` | **Cucumber obligatorio; código confirmado** |
| BDD-13e | 19 | 11 | Mismo rechazo; motivo propuesto `COMBINED_EXERCISE_UNBALANCED` | **JUnit obligatorio; código confirmado** |

**Comprobación transversal de vinculación:** BDD-05a, BDD-06, BDD-10 y BDD-13a–c también deben comprobar que la propuesta aceptada se recupera asociada a `SES-001` (o al ID de sesión indicado por el escenario). Las respuestas JSON no definen la sesión de destino.

**Alcance obligatorio de HU-03 para Cucumber:** BDD-06, BDD-07, BDD-08, BDD-09, BDD-10, BDD-11, BDD-12 y **BDD-13d**. Los restantes ejemplos de equilibrio (BDD-13a, BDD-13b, BDD-13c y BDD-13e) son **ejemplos del contrato que deben cubrirse con JUnit de dominio**; no requieren escenarios Cucumber separados. BDD-13a (15/15) está ya representado por el escenario de aceptación BDD-10. Los tests JUnit deben verificar aceptación de 15/15, 12/18 y 18/12, y rechazo de 11/19 y 19/11. La reducción de escenarios BDD no cambia las reglas de negocio, la causa de rechazo ni la exigencia de no registrar propuestas inválidas.

**Responsabilidad y secuencia del equipo:** el **alumno 1** desarrolla HU-01 y HU-02, hasta BDD-05d, y entrega una revisión GREEN con UML/commit de relevo. El **alumno 2** desarrolla los ocho escenarios obligatorios de HU-03, empezando en BDD-06 desde ese commit integrado. En un grupo individual la misma persona desarrolla ambos tramos. En las tablas anteriores se describen comportamientos; esta asignación es una condición docente, no una regla del Product Owner.

### JSON de referencia para BDD-06

```json
{
  "proposalId": "PROP-IA-001",
  "durationMinutes": 45,
  "exercises": [
    {"type": "TECHNICAL", "description": "Basic BO movements", "durationMinutes": 25, "requiredWeapons": ["BO"]},
    {"type": "PHYSICAL", "description": "Low-intensity mobility", "durationMinutes": 20, "intensity": "LOW"}
  ]
}
```

### JSON de referencia para BDD-10 y BDD-13

```json
{
  "proposalId": "PROP-IA-010",
  "durationMinutes": 30,
  "exercises": [{
    "type": "COMBINED",
    "description": "Basic mixed training",
    "durationMinutes": 30,
    "technical": {
      "description": "BO basics",
      "durationMinutes": 15,
      "requiredWeapons": ["BO"]
    },
    "physical": {
      "description": "Mobility",
      "durationMinutes": 15,
      "intensity": "LOW"
    }
  }]
}
```

Para BDD-11 se sustituye `requiredWeapons` por `["NUNCHAKU"]`; para BDD-12 se utiliza `"intensity": "HIGH"`. Para BDD-13 se cambian ambas duraciones conservando la suma 30. Esta estructura JSON de COMBINED, incluidos los campos `technical` y `physical`, **queda confirmada como contrato**; no impone una jerarquía de clases Java.

## 4. Acuerdos cerrados y exclusiones

1. **HU-02:** `EXERCISE_DURATION_MISMATCH`, `EXERCISES_REQUIRED` e `INVALID_EXERCISE_DURATION` son los códigos obligatorios de BDD-05b–d.
2. **HU-03:** `COMBINED_EXERCISE_UNBALANCED` es el código definitivo de rechazo del ejercicio combinado desequilibrado.
3. **JSON COMBINED:** estructura `exercises[]` con `type = "COMBINED"`, `technical` y `physical` tal como figura en la referencia; confirmada.
4. **Prioridad:** duración global → estructura (lista, duración individual, suma) → equilibrio → intensidad → armas; ante varios incumplimientos se informa solo el primero.
5. **BDD-08 y BDD-09:** payloads literales y respuestas de rechazo definidos en la sección 4; confirmados.
