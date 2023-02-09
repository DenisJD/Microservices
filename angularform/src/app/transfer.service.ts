import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transfer } from './transfer';
import { environment } from 'src/environments/environment';

@Injectable({providedIn: 'root'})
export class TransferService {
    private apiServerUrl = environment.transfersApiBaseUrl;

    constructor(private http: HttpClient){}

    public getTransfers(): Observable<Transfer[]> {
        return this.http.get<Transfer[]>(`${this.apiServerUrl}/transfers`);
    }

    public createTransfer(transfer: Transfer): Observable<Transfer> {
        return this.http.post<Transfer>(`${this.apiServerUrl}/transfers`, transfer);
    }

}