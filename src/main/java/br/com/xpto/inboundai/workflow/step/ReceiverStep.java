package br.com.xpto.inboundai.workflow.step;import br.com.xpto.inboundai.workflow.context.GlobalContext;import lombok.extern.slf4j.Slf4j;import org.springframework.core.annotation.Order;import org.springframework.stereotype.Component;@Slf4j@Component@Order(1)public class ReceiverStep implements WorkflowStep {	@Override	public void execute(GlobalContext context) {		log.info("ReceiverStep started");		//TODO: Here implement the logic to process the messageBody from separated question to LLM ask	}}