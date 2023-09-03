import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { faUserCircle } from '@fortawesome/free-regular-svg-icons';
import { User } from 'src/app/models/user.model';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  user: User = new User();
  faUser = faUserCircle;
  errorMessage: string = '';

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) {
    this.user = new User();
  }

  ngOnInit(): void {
    if (this.authenticationService.currentUserValue?.id_user) {
      this.router.navigate(['/profile']);
      return;
    }
  }
  login() {
    this.authenticationService.login(this.user).subscribe(
      (data) => {
        this.router.navigate(['/profile']);
        console.log(data);
        console.log(this.user);
      },
      (err) => {
        this.errorMessage = 'Username or Password is incorrect';
        console.log(err);
      }
    );
  }
}
