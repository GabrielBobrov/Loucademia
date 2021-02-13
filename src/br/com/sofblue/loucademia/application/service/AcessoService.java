package br.com.sofblue.loucademia.application.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.sofblue.loucademia.application.util.StringUtils;
import br.com.sofblue.loucademia.application.util.ValidationException;
import br.com.sofblue.loucademia.domain.acesso.Acesso;
import br.com.sofblue.loucademia.domain.acesso.AcessoRepository;
import br.com.sofblue.loucademia.domain.acesso.TipoAcesso;
import br.com.sofblue.loucademia.domain.aluno.Aluno;
import br.com.sofblue.loucademia.domain.aluno.AlunoRepository;

@Stateless
public class AcessoService {
	
	@EJB
	private AcessoRepository acessoRepository;
	
	@EJB
	private AlunoRepository alunoRepository;
	
	public TipoAcesso registrarAcesso(String matricula, Integer rg) throws ValidationException {
		
		if(StringUtils.isEmpty(matricula) && rg== null) {
			throw new ValidationException("É preciso fornecer a matrícula ou o RG do aluno");
		}
		Aluno aluno;
		if(StringUtils.isEmpty(matricula)) {
			aluno = alunoRepository.findByRG(rg);
		}else {
			aluno = alunoRepository.findByMatricula(matricula);
		}
		if(aluno == null) {
			throw new ValidationException("O aluno não foi encontrado");
		}
		Acesso ultimoAcesso = acessoRepository.findUltimoAcesso(aluno);
		TipoAcesso  tipoAcesso;
		
		if(ultimoAcesso == null || ultimoAcesso.isEntradaSaidaPreenchidas()) {
			ultimoAcesso = new Acesso();
			ultimoAcesso.setAluno(aluno);
			tipoAcesso = ultimoAcesso.registrarAcesso();
			acessoRepository.store(ultimoAcesso);
		}else {
			tipoAcesso = ultimoAcesso.registrarAcesso();
		}
		return tipoAcesso;
	}
}
