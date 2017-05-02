package com.sirma.sep.bpm.camunda.bpmn.execution.listeners;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.impl.el.Expression;
import org.camunda.bpm.engine.impl.el.FixedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sirma.itt.commons.utils.string.StringUtils;
import com.sirma.itt.seip.domain.instance.Instance;
import com.sirma.itt.seip.domain.security.ActionTypeConstants;
import com.sirma.itt.seip.instance.InstanceTypeResolver;
import com.sirma.itt.seip.instance.dao.InstanceService;
import com.sirma.itt.seip.instance.state.Operation;
import com.sirmaenterprise.sep.bpm.camunda.bpmn.ListenerExpressionUtil;
import com.sirmaenterprise.sep.bpm.camunda.listeners.delegate.CDIJavaDelegate;
import com.sirmaenterprise.sep.bpm.camunda.util.BPMInstanceUtil;

/**
 * Publishes instances from BPM transition, BPM-MUT7
 * 
 * @author simeon iliev
 */
@Singleton
public class PublishObject implements CDIJavaDelegate<PublishObject> {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private Expression source;

	private FixedValue relations;

	@Inject
	private InstanceTypeResolver instanceTypeResolver;
	@Inject
	private InstanceService instanceService;

	@Override
	public void validateParameters() {
		Objects.requireNonNull(source, "Source is a required argument ! (source)");
		Objects.requireNonNull(relations, "Relations is a required argument ! (relations)");
	}

	@Override
	public void execute(DelegateExecution delegateExecution, PublishObject sourceListener) {
		LOGGER.debug("Executes listener for execution with id {} on process {}!", delegateExecution.getId(),
				delegateExecution.getProcessInstance().getId());

		String businessId = ListenerExpressionUtil.extractBusinessIdBySourceExpression(delegateExecution,
				sourceListener.source);
		Instance sourceInstance = BPMInstanceUtil.resolveInstance(businessId, instanceTypeResolver);
		List<String> properties = Arrays
				.asList(sourceListener.relations.getExpressionText().replaceAll("\\s+", "").split(","));
		List<String> idsList = properties.stream().map(sourceInstance::getAsString)
				.filter(StringUtils::isNotNullOrEmpty).flatMap(splitPropertyValues()).collect(Collectors.toList());

		instanceTypeResolver.resolveInstances(idsList)
				.forEach(instance -> instanceService.publish(instance, new Operation(ActionTypeConstants.PUBLISH)));
	}

	public Expression getSource() {
		return source;
	}

	public void setSource(Expression source) {
		this.source = source;
	}

	public FixedValue getRelations() {
		return relations;
	}

	public void setRelations(FixedValue relations) {
		this.relations = relations;
	}

	private static Function<String, Stream<String>> splitPropertyValues() {
		return property -> Stream.of(property.replaceAll("\\{|\\}|\\[|\\]|\\(|\\)", "").split(",|¶|;"));
	}

}