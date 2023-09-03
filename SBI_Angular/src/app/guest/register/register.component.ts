import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { faUserCircle } from '@fortawesome/free-regular-svg-icons';
import { User } from 'src/app/models/user.model';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  user: User = new User();
  faUser = faUserCircle;
  errorMessage: String = '';

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (this.authenticationService.currentUserValue?.id_user) {
      this.router.navigate(['/profile']);
      return;
    }
  }

  register() {
    this.authenticationService.register(this.user).subscribe(
      (data) => {
        this.router.navigate(['/profile']);
      },
      (err) => {
        if (err?.status === 409) {
          this.errorMessage = 'Username Already Exists';
        } else {
          this.errorMessage = 'Unexpected error' + this.errorMessage;
          console.log(err);
        }
      }
    );
  }
}
