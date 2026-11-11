# 03 · Ejemplos de aceptación narrados en Given–When–Then

**Documento de conversación con el Product Owner.** Estos textos describen comportamientos que el cliente puede reconocer, reproducir y confirmar. Son el paso previo a convertir ejemplos en archivos `.feature`.

## HU-01 · Registro manual

### BDD-01a — Registrar una propuesta admisible

>**Dado** que `DOJO-01` contiene la sesión ya programada `SES-001`, con máximo 60 minutos, y el entrenador ha definido la propuesta `PROP-001` de 45 minutos; 
>
>**Cuando** solicita registrarla para ese dojo y esa sesión; 
>
>**Entonces** el sistema comunica que ha sido aceptada; 
>
>**Y** al consultar `PROP-001` devuelve duración 45 y referencias `dojoId = DOJO-01` y `sessionId = SES-001`. 

No se crea ni se programa una sesión en este escenario.

**BDD-01b, BDD-01c y BDD-01d:** repetir el comportamiento completo de aceptación y recuperación para PROP-004 de 59/60, PROP-005 de 60/60 y PROP-006 de 1/60, respectivamente. El Product Owner confirma que el límite superior es inclusivo y que el mínimo válido es 1.

### BDD-02 — Rechazar duración excesiva

>**Dado** que `DOJO-01` contiene `SES-001` con máximo 60 minutos y el entrenador ha preparado PROP-002 con duración de 75 minutos
>
>**Cuando** intenta registrarla
>
>**Entonces** el sistema la rechaza con motivo **PROPOSAL_EXCEEDS_AVAILABLE_DURATION**
>
>**Y** al consultar PROP-002 no se encuentra entre las propuestas registradas.

**BDD-02b:** con sesión 60 y PROP-007 de 61 minutos, la misma respuesta de rechazo y ausencia de registro. Este es el valor inmediatamente superior al máximo permitido.

### BDD-03 — Rechazar duración nula

>**Dado** que `DOJO-01` contiene `SES-001` con máximo 60 minutos y PROP-003 tiene duración de 0 minutos
>
>**Cuando** se solicita el registro
>
>**Entonces** se rechaza con **INVALID_PROPOSAL_DURATION**
>
>**Y** PROP-003 no puede recuperarse como registrada.

**Acuerdo de agrupación:** el equipo puede automatizar los cuatro casos válidos con un Scenario Outline y los dos excesos con otro, siempre que cada fila ejecute sus comprobaciones completas. BDD-03 puede permanecer separado para hacer explícita la regla diferente.

**Regresión HU-01/HU-02:** las propuestas básicas de HU-01 no especifican ejercicios y deben seguir siendo válidas. BDD-05c presenta expresamente una lista de ejercicios vacía en una propuesta detallada, que se rechaza. No se exige una jerarquía Java concreta para distinguir ambos casos.

## HU-02 · Viabilidad de ejercicios

### BDD-04 — Arma necesaria no disponible

>**Dado** que la sesión dura 60 minutos y está asociada a un dojo que dispone de BO y KATANA
>
>**Y** la propuesta declara 45 minutos con un ejercicio TECHNICAL de 45 minutos que requiere NUNCHAKU
>
>**Cuando** el entrenador solicita evaluar la propuesta para registrarla
>
>**Entonces** el sistema la rechaza con motivo **REQUIRED_WEAPON_UNAVAILABLE**
>
>**Y** no se registra. La única causa de este rechazo es la falta del arma requerida: los restantes datos cumplen las reglas.

### BDD-05 — Intensidad no admitida para el nivel

>**Dado** que la sesión es de nivel BEGINNER con 60 minutos
>
>**Y** la propuesta declara 45 minutos con un único ejercicio PHYSICAL de 45 minutos, intensidad HIGH
>
> **Cuando** el entrenador solicita evaluar la propuesta para registrarla
>
> **Entonces** la rechaza con **INTENSITY_NOT_ALLOWED**
>
>**Y** no queda registrada. Los demás requisitos son válidos.

**Escenarios adicionales cerrados de HU-02:** BDD-05a comprueba una propuesta mixta válida; BDD-05b comprueba suma incoherente con rechazo `EXERCISE_DURATION_MISMATCH`; BDD-05c rechaza una lista de ejercicios vacía con `EXERCISES_REQUIRED`; BDD-05d rechaza un ejercicio de cero minutos con `INVALID_EXERCISE_DURATION`. Los datos completos y la vinculación a SES-001/DOJO-01 figuran en [el contrato 02](02-aceptacion.md). No requieren negociación por grupo.

## HU-03 · Propuesta procedente de IA

### BDD-06 — Respuesta correcta y propuesta admisible

>**Dado** que el entrenador solicita preparar una sesión BEGINNER de 60 minutos en un dojo que dispone de BO
>
>**Y** el proveedor devuelve un JSON válido con PROP-IA-001, duración total 45, un ejercicio TECHNICAL de 25 minutos que requiere BO y un ejercicio PHYSICAL de 20 minutos LOW
>
>**Cuando** el entrenador solicita una propuesta al servicio y el sistema evalúa la respuesta
>
>**Entonces** la propuesta se acepta
>
>**Y** PROP-IA-001 puede recuperarse con la duración y los ejercicios recibidos.

**JSON de referencia:**

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

### BDD-07 — Prioridad del exceso total frente a la intensidad

>**Dado** que la sesión BEGINNER admite 60 minutos
>
>**Y** el servicio devuelve PROP-IA-002 de duración total 75 con un ejercicio PHYSICAL de 75 minutos de intensidad HIGH
>
>**Cuando** el entrenador solicita y evalúa la propuesta
>
>**Entonces** se rechaza con **PROPOSAL_EXCEEDS_AVAILABLE_DURATION**
>
>**Y** no se registra.

El Product Owner confirma que la duración global determina el motivo comunicado, aunque la intensidad tampoco esté admitida.

### BDD-08 — Respuesta inválida del proveedor

>**Dado** que el entrenador ha solicitado una propuesta y el proveedor devuelve un documento JSON malformado o incompatible con el contrato
>
>**Cuando** el sistema intenta procesarlo
>
>**Entonces** comunica **EXTERNAL_RESPONSE_INVALID**
>
>**Y** no registra propuesta alguna.

**Payload exacto confirmado (malformado intencionadamente; no debe corregirse):**

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

El documento no constituye JSON válido porque después de `durationMinutes` aparece un objeto sin nombre de propiedad. El sistema debe rechazarlo con `EXTERNAL_RESPONSE_INVALID` y no registrar propuestas.

### BDD-09 — Proveedor no disponible

>**Dado** que el entrenador solicita una propuesta y el proveedor está indisponible
>
>**Cuando** el sistema tramita la solicitud
>
>**Entonces** comunica **AI_SERVICE_UNAVAILABLE**
>
>**Y** no registra ninguna propuesta.

**Respuesta exacta confirmada del proveedor:**

```json
{"error": "Invalid access"}
```

Esta respuesta es un error de acceso del proveedor (no una propuesta de entrenamiento) y debe producir `AI_SERVICE_UNAVAILABLE` sin registrar datos. En las pruebas se simulará mediante un suplantador; no se realizará una llamada real a un proveedor externo.
