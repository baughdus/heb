import { Component, OnInit } from '@angular/core';
import {MongoProductsService} from '../services/mongo-products.service';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
  providers: [ DatePipe ]
})
export class SearchComponent implements OnInit {

  results: any[];

  multiDate = false;
  multiShelfLife = false;
  multiPrice = false;
  multiCost = false;

  productRequest = {
    'description': null,

    'lastSold': null,
    'minLastSold': null,
    'maxLastSold': null,

    'shelfLife': null,
    'minShelfLife': null,
    'maxShelfLife': null,

    'department': '',

    'price': null,
    'minPrice': null,
    'maxPrice': null,

    'unit': null,

    'xFor': null,

    'cost': null,
    'minCost': null,
    'maxCost': null
  };

  constructor(private mongo: MongoProductsService, private date: DatePipe) {
  }

  ngOnInit() {
    this.mongo.getAll().subscribe(res => {
      this.results = res;
    }, err => {

    });
  }

  search() {
    this.results = [];
    let request = this.convert();
    this.mongo.query(request).subscribe(res => {
      this.results = res;
    });
  }

  /**
   * Below 4 methods are simply for change the button and visible search entries
   */
  changeMultiDate() {
    this.multiDate = !this.multiDate;
    if (this.productRequest.lastSold) {
      this.productRequest.lastSold = null;
    } else {
      this.productRequest.minLastSold = null;
      this.productRequest.maxLastSold = null;
    }
  }
  changeMultiShelfLife() {
    this.multiShelfLife = !this.multiShelfLife;
    if (this.productRequest.shelfLife) {
      this.productRequest.shelfLife = null;
    } else {
      this.productRequest.minShelfLife = null;
      this.productRequest.maxShelfLife = null;
    }
  }
  changeMultiPrice() {
    this.multiPrice = !this.multiPrice;
    if (this.productRequest.price) {
      this.productRequest.price = null;
    } else {
      this.productRequest.minPrice = null;
      this.productRequest.maxPrice = null;
    }
  }
  changeMultiCost() {
    this.multiCost = !this.multiCost;
    if (this.productRequest.cost) {
      this.productRequest.cost = null;
    } else {
      this.productRequest.minCost = null;
      this.productRequest.maxCost = null;
    }
  }

  /**
   * Convert request into proper mongodb sql
   */
  convert() {
    let tmpRequest = { ...this.productRequest};
    Object.keys(tmpRequest).forEach(key => {
      let value = tmpRequest[key];
      if (value === null || value === '') {
        delete tmpRequest[key];
      } else if (key.toLocaleLowerCase().indexOf('sold') > -1) {
        tmpRequest[key] = this.date.transform(tmpRequest[key], 'yyyy-MM-dd HH:mm:ss.SSS');
      }
    });
    return tmpRequest;
  }
}
