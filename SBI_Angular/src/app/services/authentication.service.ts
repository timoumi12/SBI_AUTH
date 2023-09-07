import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../models/user.model';

const API_URL = `${environment.BASE_URL}/api/auth/`;

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  userData: any;
  public currentUser: Observable<User>;
  private headers = new HttpHeaders({'Content-Type': 'application/json'});
  private currentUserSubject: BehaviorSubject<User>; //! test if i can remove "BehaviorSubject<User>"
  constructor(private http: HttpClient) {
    let storageUser;
    const storageUserAsStr = localStorage.getItem('currentUser');
    if (storageUserAsStr) {
      storageUser = JSON.parse(storageUserAsStr);
    }
    this.currentUserSubject = new BehaviorSubject<User>(storageUser);
    this.currentUser = this.currentUserSubject.asObservable();
  }
  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }
  login(user: User): Observable<any> {
    return this.http.post<any>(API_URL + 'authenticate', user).pipe(
      map((response) => {
        if (response) {
          localStorage.setItem('currentUser', JSON.stringify(response));
          this.currentUserSubject.next(response);
        }
        return response;
      })
    );
  }

  register(user: User): Observable<any> {
    return this.http.post<any>(API_URL + 'register', user);
  }

  forgotPassword(email: string) {
    const url = `http://localhost:8080/user/password_recovery`;
    const formData = new FormData();
    formData.append('email', email);
    return this.http.post(url, formData);
  }

  processNewPass(token: string | null, password: string) {
    const url = `http://localhost:8080/user/process_password_recovery/${token}`;
    const body = { password: password, token: token};

    console.log(body);
    return this.http.post(url, body);
  }

  logout() {
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(new User());
  }
}
