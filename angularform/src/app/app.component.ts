import { Component, OnInit } from '@angular/core';
import { Account } from './account';
import { Transfer } from './transfer';
import { AccountService } from './account.service';
import { HttpErrorResponse } from '@angular/common/http';
import { TransferService } from './transfer.service';
import { NgForm } from '@angular/forms';
import { debounceTime, distinctUntilChanged, map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { NgbTypeaheadConfig } from '@ng-bootstrap/ng-bootstrap';

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
  accCodes: string[];

  constructor(private accountService: AccountService,
              private transferService: TransferService,
              private searchServise: NgbTypeaheadConfig){}

  ngOnInit() {
    this.getAccounts();
    this.getTransfers();
  }

  public getAccounts(): void {
    this.accountService.getAccounts().subscribe(
      (response: Account[]) => {
        this.accounts = response;
        this.getAccCodes();
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

  public getAccCodes(): void {
      this.accCodes = this.accounts.map((v) =>  v.code)
  }

  closeAlert() {
    this.successAlert=false;
    this.failedAlert=false;
  }

  search = (text$: Observable<string>) =>
      text$.pipe(
			debounceTime(200),
			distinctUntilChanged(),
			map((term) =>
				this.accCodes.filter((v) => v.toLowerCase().includes(term.toLocaleLowerCase())).splice(0, 10),
			),
		);

}
