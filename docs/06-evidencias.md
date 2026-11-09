# Git, colaboración, auditoría BDD/TDD y vídeo

**Instrucciones detalladas:** [guía de auditoría, UML evolutivo y vídeo](08-guia-auditoria-uml-video.md). **Plantilla rellenable:** [PLANTILLA_README_P4.md](PLANTILLA_README_P4.md). La guía desarrolla los comandos de Git, la selección de hashes, los diagramas de tres cortes y la evaluación de métricas. Esta página recoge las obligaciones esenciales.

## Estructura y ramas

Un repositorio GitHub por grupo; conservar P1/P2/P3. Partir del proyecto Maven `P4/` ya entregado, completar `README_P4.md`, features, código, pruebas, UML progresivo y evidencias. El workflow vive en `.github/workflows/p4-ci-sonar.yml` en la raíz. Trabajar con **dos etapas sucesivas**: alumno 1 y alumno 2 desarrollan HU-01 (aunque los commits los hace alumno 1), Alumno 1 desarrolla HU-02 hasta BDD-05d, deja un commit de integración GREEN con UML HU-02; alumno 2 revisa y **desde ese commit** desarrolla BDD-06–09, BDD-10–12 y BDD-13d de HU-03. Los otros casos de equilibrio se prueban en JUnit, no en nuevos escenarios Cucumber obligatorios. No se requiere desarrollo en paralelo. Conservar la trazabilidad real.

## Técnica obligatoria de auditoría y política de commits en README

**Convención común obligatoria de mensajes (en español):** `TIPO HU-XX BDD-YY: descripción concreta del cambio`. Los cinco prefijos son `ATDD-TEST` (feature, suite y glue inicial `undefined/pending`), `ATDD-FEAT` (pasos de aceptación completos), `TDD-TEST` (JUnit escrito antes del código), `TDD-FEAT` (mínima implementación GREEN) y `TDD-REFACTOR` (mejora de diseño sin cambiar reglas, incluyendo ajustes de pruebas si cambia una API interna). Se admite unir IDs cuando el mismo incremento sirve a varios escenarios: `BDD-05a+BDD-05b`; también en HU-03 se conserva siempre el ID BDD que motiva el test JUnit. El prefijo describe **el contenido del commit**, no una promesa sobre el resultado; registrar RED, GREEN y el diagnóstico **observados** por separado. No crear fallos ni refactorizaciones artificiales. Las pruebas que caracterizan conducta existente pueden ser GREEN directamente.

| Fase | Mensaje ilustrativo |
|---|---|
| Especificación ATDD | `ATDD-TEST HU-02 BDD-05b: especificar rechazo por suma de duraciones` |
| Automatización ATDD | `ATDD-FEAT HU-02 BDD-05b: verificar motivo y ausencia de registro` |
| Prueba JUnit | `TDD-TEST HU-02 BDD-05b: exigir coincidencia de duración y ejercicios` |
| Implementación | `TDD-FEAT HU-02 BDD-05b: validar suma de ejercicios` |
| Refactorización | `TDD-REFACTOR HU-02 BDD-05a+BDD-05b: trasladar validación al dominio` |

**Momento del commit:** tras escribir y ejecutar el cambio correspondiente y antes de pasar a la siguiente fase. `TDD-TEST` conserva un RED auténtico si existe, distinguiendo compilación por API aún ausente, aserción fallida y error de infraestructura; `TDD-FEAT` exige GREEN; `TDD-REFACTOR` exige GREEN antes y después. `ATDD-TEST` puede documentar `undefined/pending`, que **no** es RED de JUnit. `ATDD-FEAT` exige pasos y aserciones reales, aceptación GREEN y regresión. No fusionar una rama deliberadamente roja. **No reescribir/squash** los commits seleccionados como evidencia; vincular hash, pruebas y ejecuciones CI en `README_P4.md`. La explicación paso a paso y los comandos Git/Eclipse figuran en [el laboratorio HU-01](04-laboratorio-hu01.md) y [la guía de auditoría](08-guia-auditoria-uml-video.md).
Incluir una matriz individual y conjunta, por ejemplo:

| Alumno | BDD / HU | Cucumber inicial (hash) | JUnit RED (hash) | GREEN (hash) | REFACTOR/pruebas OO (hash) | Cucumber GREEN + regresión (hash/Actions) | PR/revisión | Minuto de vídeo |
|---|---|---|---|---|---|---|---|---|

Una ejecución de Cucumber undefined/pending es evidencia del ciclo exterior, no RED del dominio. Un test JUnit que falla por problema de entorno no acredita RED. Los cambios de reglas requieren nuevos tests; una caracterización de comportamiento existente puede ser GREEN directamente.

## Vídeo individual: núcleo de la evaluación del proceso

**Cada participante** entrega o enlaza desde README un vídeo individual de **máximo 6–7 minutos** sobre **un escenario significativo de su propia etapa**: HU-02 para alumno 1, HU-03 para alumno 2. Evitar elegir el escenario guiado inicial o una variante de valores límite que ya pase automáticamente. El escenario elegido debe tener una regla no trivial, tests internos, colaboración/refactorización relevante; ambos participantes pueden repartirse escenarios de una misma HU si queda clara la autoría.

Secuencia recomendada de demostración (no exige reproducir cada minuto de trabajo): contrato Gherkin y motivo del ejemplo; Cucumber inicial; JUnit escrito antes de producción y RED genuino; GREEN mínimo; decisiones de refactorización y pruebas OO/contrato (o justificación de no refactorizar); Cucumber completamente GREEN; regresión; dobles empleados y sus límites; lectura breve de JaCoCo/SonarQube/CI. Se deberán **mostrar hashes reales y navegar por los cambios**, no presentar solo una diapositiva o un código final. Las pruebas y el historial deben permitir reproducir las afirmaciones.

Grabación de interacción en IDE, navegador, GitHub y herramientas con voz simultánea del alumno y cursor visible; se permiten secciones y transiciones, pero cada sección es captura continua de voz y acción. No webcam obligatoria ni narración en voz en off grabada aparte. Audio inteligible y pantalla legible; vídeo en alojamiento externo (por ejemplo YouTube no listado), no subir binarios grandes al repositorio.

## Documentación evaluable en README_P4.md

- Integrantes, responsabilidades, reparto por escenarios, dependencias, revisiones y PR.
- Política de commits y técnica de auditoría con ejemplo real, tabla de trazabilidad y enlaces permanentes a hashes.
- Instrucciones de compilación y ejecución Cucumber/JUnit, workflow y resultados verificables.
- Reglas de negocio y trazabilidad HU → escenarios → tests → clases → commits.
- UML progresivo con cortes de commit tras HU-01/HU-02/HU-03, explicando qué test motivó cada cambio.
- Política de dobles: stub, mock y fake elegidos según objetivo, colaboración y limitación.
- Interpretación crítica de JaCoCo/SonarQube CalSo, hallazgos, decisiones, mejoras y reevaluaciones.
- Vídeo **por participante** con escenario, URL estable y marcas de minuto; incidencias y límites de la solución.

La evidencia de autoría es la correspondencia verificable entre el repositorio y la explicación, **no** el número bruto de commits. La rúbrica detallada y sus niveles se encuentran en `07-rubrica-analitica.md`.
