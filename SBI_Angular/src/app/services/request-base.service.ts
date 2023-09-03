import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { AuthenticationService } from './authentication.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export abstract class RequestBaseService {
  protected currentUser: User = new User();
  constructor(
    protected authenticationService: AuthenticationService,
    protected httpClient: HttpClient
  ) {
    this.authenticationService.currentUser.subscribe((data) => {
      this.currentUser = data;
    });
  }
  get headers(): HttpHeaders {
    return new HttpHeaders({
      authorization: 'Bearer ' + this.currentUser?.jwt,
      'Content-Type': 'application/json; charset=UTF-8',
    });
  }
}
