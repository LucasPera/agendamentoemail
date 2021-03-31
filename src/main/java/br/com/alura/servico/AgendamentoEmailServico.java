package br.com.alura.servico;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.com.alura.dao.AgendamentoEmailDAO;
import br.com.alura.servico.entidade.AgendamentoEmail;

@Stateless
public class AgendamentoEmailServico {
	
	private static final Logger LOGGER = Logger.getLogger(AgendamentoEmailServico.class.getName());
	
	@Inject
	private AgendamentoEmailDAO dao;
	
	public List<AgendamentoEmail> listar() {
		
		return dao.listar();
	}
	
	public List<AgendamentoEmail> listarPorNaoAgendado() {
		return dao.listarPorNaoAgendado();
	}
	
	public void inserir(AgendamentoEmail agendamentoEmail) {
		
		agendamentoEmail.setAgendado(false);
		dao.inserir(agendamentoEmail);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void alterar(AgendamentoEmail agendamentoEmail) {
		agendamentoEmail.setAgendado(true);
		dao.alterar(agendamentoEmail);
		
	}
	
	public void enviar(AgendamentoEmail agendamentoEmail) {
		
		try {
			Thread.sleep(5000);
			LOGGER.info("O e-mail do(a) usuario(a) " + agendamentoEmail.getEmail() + 
					" foi enviado");
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
		}
		
	}
}
