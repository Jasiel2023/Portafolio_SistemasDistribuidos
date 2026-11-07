import { exercises } from '../data'

function TextBlock({ label, children }) {
  return <div><span className="field-label">{label}</span><div className="info-box">{children}</div></div>
}

export default function Exercise({ index, response, updateExercise, updateAnswer }) {
  const exercise = exercises[index]
  return <article className="card">
    <div className="exercise-heading"><div className="exercise-number">{index + 1}</div><div><span className="eyebrow">Ejercicio {index + 1}</span><h2>{exercise.title}</h2></div></div>
    <TextBlock label="Objetivo">{exercise.objective}</TextBlock>
    <TextBlock label="Situación o descripción del problema">{exercise.situation}</TextBlock>
    <label className="field-label" htmlFor={`code-${index}`}>Código o pseudocódigo</label>
    <textarea id={`code-${index}`} className="code-area" placeholder="// Escribe aquí tu solución..." value={response.code} onChange={event => updateExercise(index, 'code', event.target.value)} />
    <h3>Preguntas de análisis</h3>
    {exercise.questions.map((question, questionIndex) => <div className="question" key={question}>
      <strong>{question}</strong><textarea placeholder="Escribe tu respuesta..." value={response.answers[questionIndex]} onChange={event => updateAnswer(index, questionIndex, event.target.value)} />
    </div>)}
    <div className="two-columns"><TextBlock label="Resultados esperados">{exercise.results}</TextBlock><TextBlock label="Criterios de verificación">{exercise.criteria}</TextBlock></div>
    <label className="field-label" htmlFor={`evidence-${index}`}>Evidencias</label>
    <textarea id={`evidence-${index}`} placeholder="Capturas, enlaces, resultados de pruebas o notas..." value={response.evidence} onChange={event => updateExercise(index, 'evidence', event.target.value)} />
  </article>
}
