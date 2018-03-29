import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'capitalizeEachFirstLetter'
})
export class CapitalizeEachFirstLetterPipe implements PipeTransform {
  transform(value: string, args: any[]): string {
    if (value === null) { return 'Not assigned'; }
    let valueArray: string[] = value.split(' ');
    let response = '';
    for (let tmpValue of valueArray) {
      if (response.length === 0) {
        response = tmpValue.charAt(0).toUpperCase() + tmpValue.slice(1);
      } else {
        response += ' ' + tmpValue.charAt(0).toUpperCase() + tmpValue.slice(1);
      }
    }
    return response;
  }
}
