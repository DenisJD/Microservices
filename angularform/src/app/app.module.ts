import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AccountService } from './account.service';
import { TransferService } from './transfer.service';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { NgbTypeaheadConfig, NgbTypeaheadModule } from '@ng-bootstrap/ng-bootstrap';


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    NgbTypeaheadModule
  ],
  providers: [
    AccountService, 
    TransferService,
    NgbTypeaheadConfig
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
