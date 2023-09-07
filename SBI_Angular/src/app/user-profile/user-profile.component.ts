import { Component } from '@angular/core';
import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent {
  userData: any; // Property to store user data

  constructor(private authService: AuthenticationService) {}

  ngOnInit(): void {
    // Access user data from the UserService
    console.log(this.userData);
    this.userData = this.authService.userData;
  }
}
