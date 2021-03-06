import {View, Component, Inject, NgElement} from 'app/app';
import {InstanceTypeResource} from 'form-builder/instance-type-resource/instance-type-resource';
import {InstanceSelector} from 'components/instance-selector/instance-selector';
import template from './resource.html!text';

@Component({
  selector: 'seip-resource',
  properties: {
    'formWrapper': 'form-wrapper',
    'identifier': 'identifier'
  }
})
@View({
  template: template
})
@Inject(NgElement)
export class Resource extends InstanceTypeResource {
  constructor($element) {
    super($element);
  }

  ngOnInit() {
    this.mandatoryMark = this.$element.find('.mandatory-mark');
    this.setRendered(this.$element, this.fieldViewModel.rendered);
    this.setWrapperClass(this.$element, this.fieldViewModel.preview);
    this.renderMark(this.mandatoryMark, this.renderMandatoryMark());

    this.fieldViewModelSubscription = this.fieldViewModel.subscribe('propertyChanged', (propertyChanged) => {
      let changedProperty = Object.keys(propertyChanged)[0];
      if (changedProperty === 'preview') {
        this.renderMark(this.mandatoryMark, this.renderMandatoryMark());
        return;
      }
      this.executeCommonPropertyChangedHandler(propertyChanged);
    });

    this.validationModelSubscription = this.validationModel[this.fieldViewModel.identifier].subscribe('propertyChanged', (propertyChanged) => {
      this.executeCommonPropertyChangedHandler(propertyChanged);
    });
  }
}
