import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {HttpClientModule} from '@angular/common/http';

import { AppComponent } from './app.component';
import { SearchComponent } from './search/search.component';

import {Angular2FontawesomeModule} from 'angular2-fontawesome';
import {MongoProductsService} from './services/mongo-products.service';
import {MysqlProductsService} from './services/mysql-products.service';
import {OracleProductsService} from './services/oracle-products.service';
import {FormsModule} from '@angular/forms';
import {CapitalizeEachFirstLetterPipe} from './pipes/capitalize-each-first-letter.pipe';


@NgModule({
  declarations: [
    AppComponent,
    SearchComponent,
    CapitalizeEachFirstLetterPipe
  ],
  imports: [
    BrowserModule,
    Angular2FontawesomeModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    MongoProductsService,
    MysqlProductsService,
    OracleProductsService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
