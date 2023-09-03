import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessNewPwdComponent } from './process-new-pwd.component';

describe('ProcessNewPwdComponent', () => {
  let component: ProcessNewPwdComponent;
  let fixture: ComponentFixture<ProcessNewPwdComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProcessNewPwdComponent]
    });
    fixture = TestBed.createComponent(ProcessNewPwdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
