# Requisitos técnicos y calidad

## Arquitectura hexagonal

Proyecto Maven base **ya suministrado** con Java 17, JUnit 5 y Cucumber. Se reutilizan `Weapon`, `Money`, `Discount` y `Dojo`; **`Dojo` se ofrece como raíz del agregado** con sesiones `TrainingSession` ya programadas y sus niveles `TrainingLevel`. También se proporcionan `DojoRepository` e `InMemoryDojoRepository`. El alumnado desarrolla únicamente los nuevos conceptos y casos de uso de propuestas de P4 mediante TDD. Capas: domain (conceptos, reglas, puertos de repositorio), application (casos de uso y puertos de entrada), infrastructure (adaptadores de almacenamiento, cliente IA), y adaptador de aceptación Cucumber para ejecutar casos de uso. Las dependencias apuntan hacia el núcleo; dominio no importa frameworks de test, HTTP o JSON. La arquitectura se explica con diagrama y decisiones derivadas de pruebas. No se facilita diseño UML ni jerarquía obligatoria.

## ATDD y TDD

Antes de escribir funcionalidad: ejemplo de aceptación acordado y automatizado o pendiente; durante implementación, microciclos JUnit RED/GREEN/REFACTOR. Tras completar un escenario: Cucumber GREEN real, sin Then vacíos; regresión y comprobación de cobertura. Todos los tests deben ser repetibles y deterministas.

## Dobles de prueba y política documentada

Para cada dependencia externa especificar: cuándo se usa; objetivo del test; sustituto elegido (stub para respuestas predefinidas, mock para interacciones, fake en memoria para guardar y recuperar); limitaciones y motivo. No conectar con IA real ni infraestructura de producción en tests automáticos. Verificar tanto comportamiento observable como colaboración cuando sea relevante; no confundir verificar save con persistencia comprobada.

## Herencia y polimorfismo

Evaluar especializaciones para ejercicios técnicos/físicos o propuestas; si procede herencia, implementar pruebas de contrato común, comportamiento específico y despacho polimórfico; justificar por qué la jerarquía respeta sustitución y evita duplicación. Composición puede ser válida, pero exige explicar por qué y aportar pruebas equivalentes del comportamiento. No forzar herencia meramente decorativa.

## CI y SonarQube

GitHub Actions debe ejecutar checkout, Java 17, Maven clean verify, tests JUnit y Cucumber, generar JaCoCo y subir informes. Análisis separado o posterior con SonarQube institucional https://sast.inf.um.es/ bajo perfil y Quality Gate CalSo, con secreto SONAR_TOKEN; la clave concreta del proyecto y rutas se ajustan por grupo. El workflow debe verificarse tanto en push como en pull request. Nunca imprimir tokens ni incluirlos en commits.
