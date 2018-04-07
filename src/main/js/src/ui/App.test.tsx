/// <reference path="../../node_modules/jest-enzyme/lib/index.d.ts" />

import * as React from 'react';
import * as ReactDOM from 'react-dom';
import App from './App';
import {shallow, ShallowWrapper} from 'enzyme';

it('renders without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<App/>, div);
});

describe(`<${App.name} />`, () => {
    let tree: ShallowWrapper;
    beforeAll(() => {
        tree = shallow(<App/>);
    });

    it('should display a TimeManagementView', () => {
        expect(tree.find('TimeManagementView')).toExist();
    });

    // it('snapshot', () => {
    //     expect(tree).toMatchSnapshot();
    // });
});