export interface Transfer {
    id: number;
    debitAccountCode: string;
    creditAccountCode: string;
    transferAmount: number;
    createdAt: Date;
}