import { Meta, Story } from '@storybook/react';
import Spinner from './Spinner';

export default {
  title: 'Reusable Components/Spinner',
  component: Spinner
} as Meta;

const DefaultTemplate: Story = (args) => (
  <Spinner width="10rem" placement="center" {...args}></Spinner>
);
const Default = DefaultTemplate.bind({});

const LeftTemplate: Story = (args) => <Spinner width="10rem" placement="left" {...args}></Spinner>;
const Left = LeftTemplate.bind({});

const RightTemplate: Story = (args) => (
  <Spinner width="10rem" placement="right" {...args}></Spinner>
);
const Right = RightTemplate.bind({});

export { Default, Left, Right };
