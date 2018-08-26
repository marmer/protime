import * as React from 'react';
import {TimeManagementView} from './ui/TimeManagementView';
import {UsermanagementView} from "./ui/UsermanagementView";

class App extends React.Component {
    render() {
        return (
            <div className="App">
                <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
                    <a className="navbar-brand" href="#">Protim</a>
                </nav>
                <UsermanagementView/>
                <TimeManagementView/>
            </div>
        );
    }
}

export default App;
