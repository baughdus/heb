import {Component, OnInit} from '@angular/core';
import {MongoProductsService} from '../services/mongo-products.service';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
  providers: [DatePipe]
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

  defaultRequest = {...this.productRequest};

  constructor(private mongo: MongoProductsService, private date: DatePipe) {
  }

  /**
   * Gets all the records on the database as the page is loading
   */
  ngOnInit() {
    this.mongo.getAll().subscribe(res => {
      this.results = res;
    }, err => {
      console.log(err);
    });
  }

  /**
   * Search method called from the search button upon clicking
   */
  search() {
    // Reset the results found
    this.results = [];
    // User the convert method to convert the form into a proper request
    let request = this.convert();
    // Send a POST request to the spring boot back end
    this.mongo.query(request).subscribe(res => {
      this.results = res;
    }, err => {
      console.log(err);
    });
  }

  /**
   * Resets all the fields in the search form
   */
  reset() {
    this.productRequest = {...this.defaultRequest};

    // Below 4 are required to return to a single value
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
    // Creates a copy of the current form
    let tmpRequest = {...this.productRequest};
    // Iterates over the request's fields to get their values
    Object.keys(tmpRequest).forEach(key => {
      // Get current value
      let value = tmpRequest[key];
      // Check if value is null, remove from request if it is
      if (value === null) {
        delete tmpRequest[key];
      // Checks if the field is a min/max field
      } else if (value.hasOwnProperty('isMulti')) {
        // Removes from request if neither a value or a min/max value has been added
        if (!value.value && !value.max && !value.min) {
          delete tmpRequest[key];
        // Checks if it is lastSold, due to dates needing to be sent as a certain format to the back end
        } else if (key === 'lastSold') {
          // Converts last sold to the proper date format
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
    // Returns the updated request to be sent as the body to the back end
    return tmpRequest;
  }
}
