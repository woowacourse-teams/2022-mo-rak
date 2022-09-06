import { Meta, Story } from '@storybook/react';
import Select from './Select';
import TextField from '../TextField/TextField';
import theme from '../../../styles/theme';

export default {
  title: 'Reusable Components/Select',
  component: Select
} as Meta;

const Template: Story = (args) => (
  <Select name="story" {...args}>
    <option>1</option>
    <option>2</option>
    <option>3</option>
  </Select>
);

export const Default = Template.bind({});
Default.args = {};

const TexFieldTemplate: Story = (args) => (
  <TextField
    variant="outlined"
    colorScheme={theme.colors.PURPLE_100}
    borderRadius="15px"
    padding="0.8rem"
  >
    <Select name="story" {...args}>
      <option>1</option>
      <option>2</option>
      <option>3</option>
    </Select>
  </TextField>
);

export const WithTextField = TexFieldTemplate.bind({});
WithTextField.args = {};
