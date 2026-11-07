import { controlQuestions } from '../data'

export default function Control({ answers, updateControl }) {
  return <article className="card"><span className="eyebrow">Cierre conceptual</span><h2>Preguntas de control</h2><p>Responde utilizando conceptos, ejemplos y conclusiones propias.</p>
    {controlQuestions.map((question, index) => <div className="question" key={question}><strong>{index + 1}. {question}</strong><textarea placeholder="Escribe tu respuesta..." value={answers[index]} onChange={event => updateControl(index, event.target.value)} /></div>)}
  </article>
}
