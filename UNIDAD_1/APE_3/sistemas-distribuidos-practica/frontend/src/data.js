// Reemplaza los textos TODO con el contenido de tu guía de práctica.
export const exercises = [
  {
    title: 'Control de acceso a un estacionamiento',
    objective: 'TODO: Escribe aquí el objetivo del ejercicio 1.',
    situation: 'TODO: Describe la situación problemática o caso de estudio.',
    questions: ['TODO: Pregunta de análisis 1.', 'TODO: Pregunta de análisis 2.'],
    results: 'TODO: Describe los resultados esperados.',
    criteria: 'TODO: Indica cómo se verificará el resultado.',
  },
  {
    title: 'Comunicación entre procesos',
    objective: 'TODO: Escribe aquí el objetivo del ejercicio 2.',
    situation: 'TODO: Describe el problema de comunicación, sockets, RPC o mensajería.',
    questions: ['TODO: Pregunta de análisis 1.', 'TODO: Pregunta de análisis 2.'],
    results: 'TODO: Describe los resultados esperados.',
    criteria: 'TODO: Indica cómo se verificará el resultado.',
  },
  {
    title: 'Sincronización y coordinación',
    objective: 'TODO: Escribe aquí el objetivo del ejercicio 3.',
    situation: 'TODO: Describe el problema de sincronización o coordinación.',
    questions: ['TODO: Pregunta de análisis 1.', 'TODO: Pregunta de análisis 2.'],
    results: 'TODO: Describe los resultados esperados.',
    criteria: 'TODO: Indica cómo se verificará el resultado.',
  },
  {
    title: 'Tolerancia a fallos',
    objective: 'TODO: Escribe aquí el objetivo del ejercicio 4.',
    situation: 'TODO: Describe el escenario de fallos y la solución propuesta.',
    questions: ['TODO: Pregunta de análisis 1.', 'TODO: Pregunta de análisis 2.'],
    results: 'TODO: Describe los resultados esperados.',
    criteria: 'TODO: Indica cómo se verificará el resultado.',
  },
  {
    title: 'Caso integrador',
    objective: 'TODO: Escribe aquí el objetivo del ejercicio 5.',
    situation: 'TODO: Describe el caso integrador de sistemas distribuidos.',
    questions: ['TODO: Pregunta de análisis 1.', 'TODO: Pregunta de análisis 2.'],
    results: 'TODO: Describe los resultados esperados.',
    criteria: 'TODO: Indica cómo se verificará el resultado.',
  },
]

export const controlQuestions = [
  'TODO: Pregunta general de control 1.',
  'TODO: Pregunta general de control 2.',
  'TODO: Pregunta general de control 3.',
]

export const evaluationRows = [
  ['Funcionamiento', 'TODO: Nivel excelente.', 'TODO: Nivel bueno.', 'TODO: Nivel bajo.'],
  ['Documento de práctica', 'TODO: Nivel excelente.', 'TODO: Nivel bueno.', 'TODO: Nivel bajo.'],
  ['Defensa de práctica', 'TODO: Nivel excelente.', 'TODO: Nivel bueno.', 'TODO: Nivel bajo.'],
]

export const emptyState = {
  title: 'Trabajo Práctico de Sistemas Distribuidos',
  subject: 'Sistemas Distribuidos',
  cycle: '',
  unit: '',
  members: '',
  date: '',
  exercises: exercises.map(() => ({ code: '', answers: ['', ''], evidence: '' })),
  control: ['', '', ''],
  bibliography: '',
  annexes: '',
}
