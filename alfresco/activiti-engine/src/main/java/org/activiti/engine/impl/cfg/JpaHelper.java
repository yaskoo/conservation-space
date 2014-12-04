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

package org.activiti.engine.impl.cfg;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


// TODO: Auto-generated Javadoc
/**
 * The Class JpaHelper.
 *
 * @author Tom Baeyens
 */
public class JpaHelper {

  /**
   * Creates the entity manager factory.
   *
   * @param jpaPersistenceUnitName the jpa persistence unit name
   * @return the entity manager factory
   */
  public static EntityManagerFactory createEntityManagerFactory(String jpaPersistenceUnitName) {
    return Persistence.createEntityManagerFactory(jpaPersistenceUnitName);
  }

}