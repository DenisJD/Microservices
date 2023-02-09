import { Component, OnInit } from '@angular/core';
import { Account } from './account';
import { Transfer } from './transfer';
import { AccountService } from './account.service';
import { HttpErrorResponse } from '@angular/common/http';
import { TransferService } from './transfer.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  successAlert:boolean = false;
  failedAlert:boolean = false;
  debit = '';
  credit = '';
  amount = '';
  public accounts!: Account[];
  public transfers!: Transfer[];

  constructor(private accountService: AccountService,
              private transferService: TransferService){}

  ngOnInit() {
    this.getAccounts();
    this.getTransfers();
  }

  public getAccounts(): void {
    this.accountService.getAccounts().subscribe(
      (response: Account[]) => {
        this.accounts = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public getTransfers(): void {
    this.transferService.getTransfers().subscribe(
      (response: Transfer[]) => {
        this.transfers = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    )
  }

  public onCreateTransfer(transferForm: NgForm): void {
    this.transferService.createTransfer(transferForm.value).subscribe(
      (response: Transfer) => {
        this.getAccounts();
        this.getTransfers();
        transferForm.reset();
        this.successAlert = true;
      },
      (error: HttpErrorResponse) => {
        this.failedAlert = true;
        transferForm.reset();
      }
    );
  }

  closeAlert() {
    this.successAlert=false;
    this.failedAlert=false;
  }

}
