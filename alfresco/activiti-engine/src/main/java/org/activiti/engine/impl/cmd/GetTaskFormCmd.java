/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.engine.impl.cmd;

import java.io.Serializable;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;


// TODO: Auto-generated Javadoc
/**
 * The Class GetTaskFormCmd.
 *
 * @author Tom Baeyens
 */
public class GetTaskFormCmd implements Command<TaskFormData>, Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;
  
  /** The task id. */
  protected String taskId;

  /**
   * Instantiates a new gets the task form cmd.
   *
   * @param taskId the task id
   */
  public GetTaskFormCmd(String taskId) {
    this.taskId = taskId;
  }

  /* (non-Javadoc)
   * @see org.activiti.engine.impl.interceptor.Command#execute(org.activiti.engine.impl.interceptor.CommandContext)
   */
  public TaskFormData execute(CommandContext commandContext) {
    TaskEntity task = Context
      .getCommandContext()
      .getTaskManager()
      .findTaskById(taskId);
    if (task == null) {
      throw new ActivitiException("No task found for taskId '" + taskId +"'");
    }
    
    if(task.getTaskDefinition() != null) {
      TaskFormHandler taskFormHandler = task.getTaskDefinition().getTaskFormHandler();
      if (taskFormHandler == null) {
        throw new ActivitiException("No taskFormHandler specified for task '" + taskId +"'");
      }
      
      return taskFormHandler.createTaskForm(task);
    } else {
      // Standalone task, no TaskFormData available
      return null;
    }
  }

}
