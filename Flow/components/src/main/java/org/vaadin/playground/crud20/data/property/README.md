My vision is that instead of properties, we would be working with *signals*. I'm calling them properties here in case my
vision differs greatly from Leif's.

The intention is that you can do everything with properties and a single field that you currently do with Binder and
multiple fields. The new binder would be a collection of properties, hidden behind a user-friendly API.

Random notes:

- Conversion and Validation are currently treated separately, with separate APIs that are not really consistent. It
  would be great if these could be unified. I've made some attempts regarding state management but more could probably
  be done.
- Continuing on the previous bullet point, there may be other concepts that are duplicated throughout the code and could
  be unified and simplified once identified.
- Think about what parts of the code are intended to be extended by users and in what way. Should it be possible to
  extend every class and override every method, or should something be `final`? Should it be possible to plug in
  strategies to allow users to change the behavior without overriding?
- Using many properties means using more memory. What can be done to reduce the memory footprint of the properties?
- Using many mapped properties means doing more computations. What can be done to reduce the CPU usage of the
  properties?
- What is the best way to avoid memory leaks? More weak references or more controlled registrations/removals?
- Especially when chaining properties together, there may be "extra" events flowing around.
- The handling of empty values feels a bit chaotic.

Features that could be added:

- Is there a need for vetoable property changes, where a listener can veto a change before the property value is
  updated?
