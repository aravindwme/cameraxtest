import React, {useState} from 'react';
import {NativeModules, Button, View, Text} from 'react-native';

const NewModuleButton = () => {
  const {CalendarModule} = NativeModules;
  const [currYear, setCurrYear] = useState(0);
  const onPress = () => {
    console.log('Invoke Native Module Here!');
    CalendarModule.createCalendarEvent('testName', 'testLocation', eventId => {
      console.log(`Created a new event with id ${eventId}`);
      setCurrYear(eventId);
    });
  };

  return (
    <View style={{display: 'flex', flexDirection: 'column'}}>
      <Button
        title="Click to invoke your native module!"
        color="#841584"
        onPress={onPress}
      />
      <Text style={{marginTop: 20}}>{currYear}</Text>
    </View>
  );
};

export default NewModuleButton;
