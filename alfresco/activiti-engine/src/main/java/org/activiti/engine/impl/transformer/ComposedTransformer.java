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
package org.activiti.engine.impl.transformer;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * Applies a list of transformers to the input object.
 *
 * @author Esteban Robles Luna
 */
public class ComposedTransformer extends AbstractTransformer {

  /** The transformers. */
  protected List<Transformer> transformers;
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected Object primTransform(Object anObject) throws Exception {
    Object current = anObject;
    for (Transformer transformer : this.transformers) {
      current = transformer.transform(current);
    }
    return current;
  }
}
