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

package org.activiti.engine.impl.persistence.entity;

import java.util.List;

import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.persistence.AbstractHistoricManager;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Event;


// TODO: Auto-generated Javadoc
/**
 * The Class CommentManager.
 *
 * @author Tom Baeyens
 */
public class CommentManager extends AbstractHistoricManager {
  
  /* (non-Javadoc)
   * @see org.activiti.engine.impl.persistence.AbstractManager#delete(org.activiti.engine.impl.db.PersistentObject)
   */
  public void delete(PersistentObject persistentObject) {
    checkHistoryEnabled();
    super.delete(persistentObject);
  }

  /* (non-Javadoc)
   * @see org.activiti.engine.impl.persistence.AbstractManager#insert(org.activiti.engine.impl.db.PersistentObject)
   */
  public void insert(PersistentObject persistentObject) {
    checkHistoryEnabled();
    super.insert(persistentObject);
  }

  /**
   * Find comments by task id.
   *
   * @param taskId the task id
   * @return the list
   */
  @SuppressWarnings("unchecked")
  public List<Comment> findCommentsByTaskId(String taskId) {
    checkHistoryEnabled();
    return getDbSqlSession().selectList("selectCommentsByTaskId", taskId);
  }

  /**
   * Find events by task id.
   *
   * @param taskId the task id
   * @return the list
   */
  @SuppressWarnings("unchecked")
  public List<Event> findEventsByTaskId(String taskId) {
    checkHistoryEnabled();
    return getDbSqlSession().selectList("selectEventsByTaskId", taskId);
  }

  /**
   * Delete comments by task id.
   *
   * @param taskId the task id
   */
  public void deleteCommentsByTaskId(String taskId) {
    checkHistoryEnabled();
    getDbSqlSession().delete("deleteCommentsByTaskId", taskId);
  }

  /**
   * Find comments by process instance id.
   *
   * @param processInstanceId the process instance id
   * @return the list
   */
  @SuppressWarnings("unchecked")
  public List<Comment> findCommentsByProcessInstanceId(String processInstanceId) {
    checkHistoryEnabled();
    return getDbSqlSession().selectList("selectCommentsByProcessInstanceId", processInstanceId);
  }

}
