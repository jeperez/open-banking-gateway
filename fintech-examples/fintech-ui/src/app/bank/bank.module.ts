import { NgModule } from '@angular/core';
import { BankComponent } from './bank.component';
import { BankRoutingModule } from './bank-routing.module';
import { ShareModule } from '../common/share.module';
import { CommonModule } from '@angular/common';
import { ListAccountsModule } from './list-accounts/list-accounts.module';
import { SidebarComponent } from './sidebar/sidebar.component';

@NgModule({
  declarations: [SidebarComponent, BankComponent],
  imports: [CommonModule, ShareModule, BankRoutingModule, ListAccountsModule]
})
export class BankModule {}
