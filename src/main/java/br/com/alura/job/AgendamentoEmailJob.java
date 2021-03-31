package br.com.alura.job;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

import br.com.alura.servico.AgendamentoEmailServico;
import br.com.alura.servico.entidade.AgendamentoEmail;

@Singleton //retorna um unica instancia do objeto
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AgendamentoEmailJob {
	
	@Inject
	private AgendamentoEmailServico agendamentoEmailServico;
	
	@Inject
	@JMSConnectionFactory("java:jboss/DefaultJMSConnectionFactory")
	private JMSContext context;
	
	@Resource(mappedName = "java:/jms/queue/EmailQueue")
	private Queue queue;
	
	@Schedule(hour = "*", minute = "*", second = "*/10") //envia a cada 10 segundos
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void enviarEmail() {
		List<AgendamentoEmail> agendamentoEmailList = agendamentoEmailServico.listarPorNaoAgendado();
		
		agendamentoEmailList.forEach(emailNaoAgendado -> {
			context.createProducer().send(queue, emailNaoAgendado); //envia pra fila
			agendamentoEmailServico.alterar(emailNaoAgendado);
		});
	}
	
}
