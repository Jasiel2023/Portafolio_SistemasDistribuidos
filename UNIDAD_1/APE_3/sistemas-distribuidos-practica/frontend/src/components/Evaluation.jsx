import { evaluationRows } from '../data'

export default function Evaluation({ data, updateField }) {
  return <article className="card"><span className="eyebrow">Autoevaluación</span><h2>Evaluación</h2>
    <div className="table-wrap"><table><thead><tr><th>Criterio</th><th>Excelente</th><th>Bueno</th><th>Bajo</th></tr></thead><tbody>{evaluationRows.map(row => <tr key={row[0]}>{row.map((cell, index) => <td key={cell}>{index === 0 ? <strong>{cell}</strong> : cell}</td>)}</tr>)}</tbody></table></div>
    <label className="field-label">Bibliografía</label><textarea placeholder="Libros, artículos, documentación y enlaces..." value={data.bibliography} onChange={event => updateField('bibliography', event.target.value)} />
    <label className="field-label">Anexos</label><textarea placeholder="Enlaces a GitHub, videos, capturas o material adicional..." value={data.annexes} onChange={event => updateField('annexes', event.target.value)} />
  </article>
}
