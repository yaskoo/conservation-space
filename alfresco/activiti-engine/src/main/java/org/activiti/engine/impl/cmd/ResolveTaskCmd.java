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
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.TaskEntity;


// TODO: Auto-generated Javadoc
/**
 * The Class ResolveTaskCmd.
 *
 * @author Tom Baeyens
 */
public class ResolveTaskCmd extends CompleteTaskCmd implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new resolve task cmd.
   *
   * @param taskId the task id
   * @param variables the variables
   */
  public ResolveTaskCmd(String taskId, Map<String, Object> variables) {
    super(taskId, variables);
  }

  /* (non-Javadoc)
   * @see org.activiti.engine.impl.cmd.CompleteTaskCmd#completeTask(org.activiti.engine.impl.persistence.entity.TaskEntity)
   */
  @Override
  protected void completeTask(TaskEntity task) {
    task.resolve();
  }
}
