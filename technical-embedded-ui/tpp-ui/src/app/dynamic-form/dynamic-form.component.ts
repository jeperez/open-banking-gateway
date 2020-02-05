import { Component, Input, OnInit }  from '@angular/core';
import { FormGroup } from '@angular/forms';
import { DynamicFormControlBase } from './dynamic-form-control-base';
import {DynamicFormFactory} from "./dynamic-form-factory";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'dynamic-form',
  templateUrl: './dynamic-form.component.html'
})
export class DynamicFormComponent implements OnInit {

  @Input() controlTemplates: DynamicFormControlBase<any>[] = [];
  @Input() submissionUri: string;
  form: FormGroup;

  constructor(private client: HttpClient, private formFactory: DynamicFormFactory) {  }

  ngOnInit() {
    this.form = this.formFactory.toFormGroup(this.controlTemplates);
  }

  save() {
    const formObj = this.form.getRawValue();
    this.client.post(
      this.submissionUri,
      formObj,
      {headers: {
        'X-Request-ID': this.uuidv4(),
        'X-XSRF-TOKEN': this.uuidv4(),
      }}
    ).subscribe(res => {
    });
  }

  uuidv4(): string {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
      let r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
      return v.toString(16);
    });
  }
}