package com.localaem.core.servlets;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.model.WorkflowModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;


@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION+"=ExecuteWorkflowServlet",
        "sling.servlet.paths=/bin/practice/executeworkflow",
        "sling.servlet.methods="+ HttpConstants.METHOD_GET
})
public class ExecuteWorkflowServlet extends SlingSafeMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteWorkflowServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response){
        try {
            ResourceResolver resourceResolver = request.getResourceResolver();
            WorkflowSession workflowSession = resourceResolver.adaptTo(WorkflowSession.class);
            LOGGER.info("ExecuteWorkflowServlet :: workflowSession : {}",workflowSession);
            WorkflowModel workflowModel = workflowSession.getModel("/var/workflow/models/aem-local-test-model");
            LOGGER.info("ExecuteWorkflowServlet :: workflowModel : {}",workflowModel);
            WorkflowData workflowData = workflowSession.newWorkflowData("JCR_PATH",request.getParameter("path").toString());
            LOGGER.info("ExecuteWorkflowServlet :: workflowData : {}",workflowData);
            workflowSession.startWorkflow(workflowModel,workflowData);
            response.getWriter().write("Execute Practice Workflow Success");
        } catch (IOException | WorkflowException e) {
            LOGGER.error("Exception in ExecuteWorkflowServlet :: {}",e);
        }
    }
}
