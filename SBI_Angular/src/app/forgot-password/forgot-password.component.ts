import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  user: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthenticationService,
    private router: Router) {
      this.user = formBuilder.group({
        "email": ['', Validators.required]
      });
    }

  ngOnInit() { }

  onSubmit() {
    const email = this.user.value.email; // Get the email from the form
    if (!email) {
      // Handle the case where email is not provided
      return;
    }
    this.authService.forgotPassword(email)
      .subscribe(result => {
        console.log(result);
        this.router.navigate(['/login']);
      });
  }

}