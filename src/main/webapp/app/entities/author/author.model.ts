import { BaseEntity } from './../../shared';

export class Author implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public birthDate?: any,
        public author_books?: BaseEntity[],
    ) {
    }
}
