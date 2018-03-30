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

  results;

  productRequest = {
    'description': null,
    'lastSold': {
      'isMulti': false,
      'value': null,
      'min': null,
      'max': null
    },
    'shelfLife': {
      'isMulti': false,
      'value': null,
      'min': null,
      'max': null
    },
    'department': null,
    'price': {
      'isMulti': false,
      'value': null,
      'min': null,
      'max': null
    },
    'unit': null,
    'xFor': null,
    'cost': {
      'isMulti': false,
      'value': null,
      'min': null,
      'max': null
    }
  };

  defaultRequest = { ...this.productRequest};

  constructor(private mongo: MongoProductsService, private date: DatePipe) {
  }

  ngOnInit() {
    this.mongo.getAll().subscribe(res => {
      this.results = res;
    }, err => {
      console.log(err);
    });
  }

  search() {
    this.results = [];
    let request = this.convert();
    this.mongo.query(request).subscribe(res => {
      this.results = res;
    }, err => {
      console.log(err);
    });
  }

  reset() {
    this.productRequest = { ...this.defaultRequest };
    this.productRequest.cost.isMulti = false;
    this.productRequest.price.isMulti = false;
    this.productRequest.lastSold.isMulti = false;
    this.productRequest.shelfLife.isMulti = false;
  }

  /**
   * Below 4 methods are simply for change the button and visible search entries
   */
  changeMultiDate() {
    this.productRequest.lastSold.isMulti = !this.productRequest.lastSold.isMulti;
    if (this.productRequest.lastSold.isMulti) {
      this.productRequest.lastSold.value = null;
    } else {
      this.productRequest.lastSold.min = null;
      this.productRequest.lastSold.max = null;
    }
  }
  changeMultiShelfLife() {
    this.productRequest.shelfLife.isMulti = !this.productRequest.shelfLife.isMulti;
    if (this.productRequest.shelfLife.isMulti) {
      this.productRequest.shelfLife.value = null;
    } else {
      this.productRequest.shelfLife.min = null;
      this.productRequest.shelfLife.max = null;
    }
  }
  changeMultiPrice() {
    this.productRequest.price.isMulti = !this.productRequest.price.isMulti;
    if (this.productRequest.price.isMulti) {
      this.productRequest.price.value = null;
    } else {
      this.productRequest.price.min = null;
      this.productRequest.price.max = null;
    }
  }
  changeMultiCost() {
    this.productRequest.cost.isMulti = !this.productRequest.cost.isMulti;
    if (this.productRequest.cost.isMulti) {
      this.productRequest.cost.value = null;
    } else {
      this.productRequest.cost.min = null;
      this.productRequest.cost.max = null;
    }
  }

  /**
   * Convert request into proper mongodb sql
   */
  convert() {
    let tmpRequest = { ...this.productRequest};
    Object.keys(tmpRequest).forEach(key => {
      let value = tmpRequest[key];
      if (value === null) {
        delete tmpRequest[key];
      } else if (value.hasOwnProperty('isMulti')) {
        if (!value.value && !value.max && !value.min) {
          delete tmpRequest[key];
        } else if (key === 'lastSold') {
          let lastSold = {
              'isMulti': tmpRequest[key].isMulti,
              'value': tmpRequest[key].value !== null ? this.date.transform(tmpRequest[key].value, 'yyyy-MM-dd HH:mm:ss.SSS') : null,
              'min': tmpRequest[key].min !== null ? this.date.transform(tmpRequest[key].min, 'yyyy-MM-dd HH:mm:ss.SSS') : null,
              'max': tmpRequest[key].max !== null ? this.date.transform(tmpRequest[key].max, 'yyyy-MM-dd HH:mm:ss.SSS') : null
          };
          tmpRequest[key] = lastSold;
        }
      }
    });
    console.log(this.productRequest, tmpRequest);
    return tmpRequest;
  }
}
