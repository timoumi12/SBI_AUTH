import { Component } from '@angular/core';
import { AuthenticationService } from '../services/authentication.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-process-new-pwd',
  templateUrl: './process-new-pwd.component.html',
  styleUrls: ['./process-new-pwd.component.css']
})
export class ProcessNewPwdComponent {
  token: string | null = null;
  newPassword: string = '';
  formData: { 
    password: string, 
    confirm_password: string 
  } = {
     password: '', 
     confirm_password: '' 
    };
  passMismatch = false;

  constructor(
    private authService: AuthenticationService,
    private route: ActivatedRoute,
    private router: Router) {
    }
    ngOnInit() {
        this.route.params.subscribe(params => {
            this.token = this.route.snapshot.queryParamMap.get('token');
        });
    }

    onSubmit() {
        if (this.formData.password !== this.formData.confirm_password) {
          this.passMismatch = true;
        } else {
          this.authService.processNewPass(this.token, this.formData.password)
            .subscribe(result => {
              console.log(result);
              this.router.navigate(['/login']);
            });
        }
    }
}
