import styled from '@emotion/styled';
import { Meta, Story } from '@storybook/react';
import theme from '../../styles/theme';
import FlexContainer from './FlexContainer';

export default {
  title: 'Reusable Components/FlexContainer',
  component: FlexContainer
} as Meta;

const SampleBox = styled.div`
  width: 10rem;
  height: 10rem;
  border: 1px solid ${theme.colors.BLACK_100};
`;
const DefaultTemplate: Story = (args) => (
  <FlexContainer {...args}>
    <SampleBox />
    <SampleBox />
    <SampleBox />
  </FlexContainer>
);

const Default = DefaultTemplate.bind({});

const ColumnTemplate: Story = (args) => (
  <FlexContainer flexDirection="column" {...args}>
    <SampleBox />
    <SampleBox />
    <SampleBox />
  </FlexContainer>
);
const Column = ColumnTemplate.bind({});

export { Default, Column };
