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

package org.activiti.engine.impl.pvm.runtime;

import java.util.logging.Logger;

import org.activiti.engine.impl.pvm.PvmTransition;

// TODO: Auto-generated Javadoc
/**
 * The Class OutgoingExecution.
 *
 * @author Tom Baeyens
 * @author Daniel Meyer
 */
public class OutgoingExecution {
  
  /** The log. */
  private static Logger log = Logger.getLogger(OutgoingExecution.class.getName());
  
  /** The outgoing execution. */
  protected InterpretableExecution outgoingExecution;
  
  /** The outgoing transition. */
  protected PvmTransition outgoingTransition;
  
  /** The is new. */
  protected boolean isNew;

  /**
   * Instantiates a new outgoing execution.
   *
   * @param outgoingExecution the outgoing execution
   * @param outgoingTransition the outgoing transition
   * @param isNew the is new
   */
  public OutgoingExecution(InterpretableExecution outgoingExecution, PvmTransition outgoingTransition, boolean isNew) {
    this.outgoingExecution = outgoingExecution;
    this.outgoingTransition = outgoingTransition;
    this.isNew = isNew;
  }
  
  /**
   * Take.
   */
  public void take() {
    if (outgoingExecution.getReplacedBy()!=null) {
      outgoingExecution = outgoingExecution.getReplacedBy();
    }    
    if(!outgoingExecution.isDeleteRoot()) {
      outgoingExecution.take(outgoingTransition);
    } else {
      log.fine("Not taking transition '"+outgoingTransition+"', outgoing execution has ended.");      
    }
  }
}